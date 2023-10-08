package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.PlayerTickEvent;
import legitish.module.Module;
import legitish.module.modules.client.Targets;
import legitish.module.modulesettings.impl.ModuleComboSetting;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleDoubleSliderSetting;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.utils.CooldownUtils;
import legitish.utils.GameUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class SprintReset extends Module {
    public static ModuleSliderSetting range, chance;
    public static ModuleDoubleSliderSetting actionTicks;
    public static ModuleComboSetting timing, mode;

    public static CooldownUtils actionTimer = new CooldownUtils(0), postDelayTimer = new CooldownUtils(0);
    public static boolean comboing, alreadyHit, waitingForPostDelay;

    public SprintReset() {
        super("Sprint Reset", category.Combat, 0);
        this.registerSetting(new ModuleDesc("Resets your sprint."));
        ArrayList<String> modes = new ArrayList<>(Arrays.asList("WTap", "STap", "Blockhit"));
        this.registerSetting(mode = new ModuleComboSetting("Mode", "WTap", modes));
        this.registerSetting(actionTicks = new ModuleDoubleSliderSetting("Action Time (MS)", 25, 55, 1, 500, 1));
        this.registerSetting(chance = new ModuleSliderSetting("Chance %", 100, 0, 100, 1));
        this.registerSetting(range = new ModuleSliderSetting("Range: ", 3, 1, 6, 0.05));
        ArrayList<String> timings = new ArrayList<>(Arrays.asList("Pre", "Post"));
        this.registerSetting(timing = new ModuleComboSetting("Reset timing", "Post", timings));
    }

    private static void finishCombo() {
        if (mode.getValue().equals("WTap")) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), true);
        } else if (mode.getValue().equals("STap")) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
        } else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
        }
    }

    private static void startCombo() {
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
            if (mode.getValue().equals("WTap")) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
                KeyBinding.onTick(mc.gameSettings.keyBindForward.getKeyCode());
            } else if (mode.getValue().equals("STap")) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), true);
                KeyBinding.onTick(mc.gameSettings.keyBindBack.getKeyCode());
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                KeyBinding.onTick(mc.gameSettings.keyBindUseItem.getKeyCode());
            }
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = PlayerTickEvent.class)
    public void onTick(PlayerTickEvent event) {
        if (GameUtils.isPlayerInGame()) {
            if (waitingForPostDelay) {
                if (postDelayTimer.hasFinished()) {
                    waitingForPostDelay = false;
                    comboing = true;
                    startCombo();
                    actionTimer.start();
                }
                return;
            }

            if (comboing) {
                if (actionTimer.hasFinished()) {
                    comboing = false;
                    finishCombo();
                }
                return;
            }

            if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && Mouse.isButtonDown(0)) {
                Entity target = mc.objectMouseOver.entityHit;
                if (target.isDead) {
                    return;
                }

                if (mc.thePlayer.getDistanceToEntity(target) <= range.getInput()) {
                    if ((target.hurtResistantTime >= 10 && Objects.equals(timing.getValue(), "Post")) || (target.hurtResistantTime <= 10 && Objects.equals(timing.getValue(), "Pre"))) {
                        if (!(target instanceof EntityPlayer)) {
                            return;
                        }

                        if (!Targets.isTarget((EntityPlayer) target)) {
                            return;
                        }

                        if (!(chance.getInput() == 100 || Math.random() <= chance.getInput() / 100))
                            return;

                        if (!alreadyHit) {
                            actionTimer.setCooldown((long) ThreadLocalRandom.current().nextDouble(actionTicks.getInputMin(), actionTicks.getInputMax() + 0.01));
                            comboing = true;
                            startCombo();
                            actionTimer.start();
                            alreadyHit = true;
                        }
                    } else {
                        alreadyHit = false;
                    }
                }
            }
        }
    }
}
