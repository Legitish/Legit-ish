package legitish.module.modules.combat;

import legitish.main.Legitish;
import legitish.module.Module;
import legitish.module.ModuleDesc;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Dragclicker extends Module {
    public static ModuleDesc moduleDesc;
    public static ModuleSliderSetting clicks;
    public static ModuleSliderSetting delay;
    public static ModuleTickSetting delayRandomizer;
    public static ModuleTickSetting placeWhenBlock;
    private boolean l_c = false;
    private boolean l_r = false;
    private Method rightClickMouse = null;

    public Dragclicker() {
        super("Dragclicker", Module.category.Combat, 0);
        this.registerSetting(moduleDesc = new ModuleDesc("Artificial dragclicking."));
        this.registerSetting(clicks = new ModuleSliderSetting("Clicks", 0.0D, 0.0D, 50.0D, 1.0D));
        this.registerSetting(delay = new ModuleSliderSetting("Delay (ms)", 5.0D, 1.0D, 40.0D, 1.0D));
        this.registerSetting(delayRandomizer = new ModuleTickSetting("Delay randomizer", true));
        this.registerSetting(placeWhenBlock = new ModuleTickSetting("Place when block", false));

        try {
            this.rightClickMouse = mc.getClass().getDeclaredMethod("func_147121_ag");
        } catch (NoSuchMethodException var4) {
            try {
                this.rightClickMouse = mc.getClass().getDeclaredMethod("rightClickMouse");
            } catch (NoSuchMethodException ignored) {
            }
        }

        if (this.rightClickMouse != null) {
            this.rightClickMouse.setAccessible(true);
        }

    }

    public void onEnable() {
        if (clicks.getInput() != 0.0D && mc.currentScreen == null && mc.inGameHasFocus) {
            Legitish.getExecutor().execute(() -> {
                try {
                    int cl = (int) clicks.getInput();
                    int del = (int) delay.getInput();

                    for (int i = 0; i < cl * 2 && this.isEnabled() && GameUtils.isPlayerInGame() && mc.currentScreen == null && mc.inGameHasFocus; ++i) {
                        if (i % 2 == 0) {
                            this.l_c = true;
                            if (del != 0) {
                                int realDel = del;
                                if (delayRandomizer.isToggled()) {
                                    realDel = del + MouseUtils.rand().nextInt(25) * (MouseUtils.rand().nextBoolean() ? -1 : 1);
                                    if (realDel <= 0) {
                                        realDel = del / 3 - realDel;
                                    }
                                }

                                Thread.sleep(realDel);
                            }
                        } else {
                            this.l_r = true;
                        }
                    }

                    this.disable();
                } catch (InterruptedException ignored) {
                }

            });
        } else {
            this.disable();
        }
    }

    public void onDisable() {
        this.l_c = false;
        this.l_r = false;
    }

    public void update() {
        if (GameUtils.isPlayerInGame()) {
            if (this.l_c) {
                this.c(true);
                this.l_c = false;
            } else if (this.l_r) {
                this.c(false);
                this.l_r = false;
            }
        }

    }

    private void c(boolean st) {
        boolean r = placeWhenBlock.isToggled() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock;
        if (r) {
            try {
                this.rightClickMouse.invoke(mc);
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
        } else {
            int key = mc.gameSettings.keyBindAttack.getKeyCode();
            KeyBinding.setKeyBindState(key, st);
            if (st) {
                KeyBinding.onTick(key);
            }
        }

        MouseUtils.sc(r ? 1 : 0, st);
    }
}
