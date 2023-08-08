package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.PlayerTickEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleDoubleSliderSetting;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MathUtils;
import legitish.utils.MouseUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

public class AutoClicker extends Module {
    public static ModuleDesc desc;
    public static ModuleDoubleSliderSetting CPS;
    public static ModuleSliderSetting jitter;
    public static ModuleTickSetting weaponOnly, blocksOnly, breakBlocks, leftClick, rightClick, inventoryFill;
    private Random rand = null;
    private Method gs;
    private long i, j, k, l;
    private double m;
    private boolean n, hol;

    public AutoClicker() {
        super("Autoclicker", category.Combat, 0);
        this.registerSetting(desc = new ModuleDesc("Clicks for you."));
        this.registerSetting(CPS = new ModuleDoubleSliderSetting("CPS", 10.0D, 12.0D, 1.0D, 20.0D, 0.5D));
        this.registerSetting(jitter = new ModuleSliderSetting("Jitter", 0.0D, 0.0D, 3.0D, 0.1D));
        this.registerSetting(leftClick = new ModuleTickSetting("Left click", true));
        this.registerSetting(rightClick = new ModuleTickSetting("Right click", false));
        this.registerSetting(inventoryFill = new ModuleTickSetting("Inventory fill", false));
        this.registerSetting(weaponOnly = new ModuleTickSetting("Weapon only", false));
        this.registerSetting(blocksOnly = new ModuleTickSetting("Blocks only", false));
        this.registerSetting(breakBlocks = new ModuleTickSetting("Break blocks", false));

        try {
            this.gs = GuiScreen.class.getDeclaredMethod("mouseClicked", Integer.TYPE, Integer.TYPE, Integer.TYPE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (this.gs != null) {
            this.gs.setAccessible(true);
        }
    }

    public void onEnable() {
        if (this.gs == null) {
            this.disable();
        }

        this.rand = new Random();
    }

    public void onDisable() {
        this.i = 0L;
        this.j = 0L;
        this.hol = false;
    }

    public void guiUpdate() {
        MathUtils.b(CPS);
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = PlayerTickEvent.class)
    public void onTick(PlayerTickEvent event) {
        if (GameUtils.isPlayerInGame() && !mc.thePlayer.isEating()) {
            if (mc.currentScreen == null && mc.inGameHasFocus) {
                if (weaponOnly.isToggled() && !GameUtils.getWeapon()) {
                    return;
                }

                Mouse.poll();
                if (leftClick.isToggled() && Mouse.isButtonDown(0)) {
                    this.autoClick(mc.gameSettings.keyBindAttack.getKeyCode(), 0);
                } else if (rightClick.isToggled() && Mouse.isButtonDown(1)) {
                    if (blocksOnly.isToggled() && !(mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock)) {
                        return;
                    }
                    this.autoClick(mc.gameSettings.keyBindUseItem.getKeyCode(), 1);
                } else {
                    this.i = 0L;
                    this.j = 0L;
                }
            } else if (inventoryFill.isToggled() && mc.currentScreen instanceof GuiInventory) {
                if (!Mouse.isButtonDown(0) || !Keyboard.isKeyDown(54) && !Keyboard.isKeyDown(42)) {
                    this.i = 0L;
                    this.j = 0L;
                } else if (this.i != 0L && this.j != 0L) {
                    if (System.currentTimeMillis() > this.j) {
                        this.gd();
                        this.ci(mc.currentScreen);
                    }
                } else {
                    this.gd();
                }
            }

        }
    }

    public void autoClick(int key, int mouse) {
        if (breakBlocks.isToggled() && mouse == 0 && mc.objectMouseOver != null) {
            BlockPos p = mc.objectMouseOver.getBlockPos();
            if (p != null) {
                Block bl = mc.theWorld.getBlockState(p).getBlock();
                if (bl != Blocks.air && !(bl instanceof BlockLiquid)) {
                    if (!this.hol) {
                        KeyBinding.setKeyBindState(key, true);
                        KeyBinding.onTick(key);
                        this.hol = true;
                    }
                    return;
                }

                if (this.hol) {
                    KeyBinding.setKeyBindState(key, false);
                    this.hol = false;
                }
            }
        }

        if (jitter.getInput() > 0.0D) {
            double a = jitter.getInput() * 0.45D;
            EntityPlayerSP var10000;
            if (this.rand.nextBoolean()) {
                var10000 = mc.thePlayer;
                var10000.rotationYaw = (float) ((double) var10000.rotationYaw + (double) this.rand.nextFloat() * a);
            } else {
                var10000 = mc.thePlayer;
                var10000.rotationYaw = (float) ((double) var10000.rotationYaw - (double) this.rand.nextFloat() * a);
            }

            if (this.rand.nextBoolean()) {
                var10000 = mc.thePlayer;
                var10000.rotationPitch = (float) ((double) var10000.rotationPitch + (double) this.rand.nextFloat() * a * 0.45D);
            } else {
                var10000 = mc.thePlayer;
                var10000.rotationPitch = (float) ((double) var10000.rotationPitch - (double) this.rand.nextFloat() * a * 0.45D);
            }
        }

        if (this.j > 0L && this.i > 0L) {
            if (System.currentTimeMillis() > this.j) {
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                MouseUtils.sendClick(mouse, true);
                this.gd();
            } else if (System.currentTimeMillis() > this.i) {
                KeyBinding.setKeyBindState(key, false);
                MouseUtils.sendClick(mouse, false);
            }
        } else {
            this.gd();
        }

    }

    public void gd() {
        double c = MathUtils.mmVal(CPS, this.rand) + 0.4D * this.rand.nextDouble();
        long d = (int) Math.round(1000.0D / c);
        if (System.currentTimeMillis() > this.k) {
            if (!this.n && this.rand.nextInt(100) >= 85) {
                this.n = true;
                this.m = 1.1D + this.rand.nextDouble() * 0.15D;
            } else {
                this.n = false;
            }

            this.k = System.currentTimeMillis() + 500L + (long) this.rand.nextInt(1500);
        }

        if (this.n) {
            d = (long) ((double) d * this.m);
        }

        if (System.currentTimeMillis() > this.l) {
            if (this.rand.nextInt(100) >= 80) {
                d += 50L + (long) this.rand.nextInt(100);
            }

            this.l = System.currentTimeMillis() + 500L + (long) this.rand.nextInt(1500);
        }

        this.j = System.currentTimeMillis() + d;
        this.i = System.currentTimeMillis() + d / 2L - (long) this.rand.nextInt(10);
    }

    private void ci(GuiScreen s) {
        int x = Mouse.getX() * s.width / mc.displayWidth;
        int y = s.height - Mouse.getY() * s.height / mc.displayHeight - 1;

        try {
            this.gs.invoke(s, x, y, 0);
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }

    }
}
