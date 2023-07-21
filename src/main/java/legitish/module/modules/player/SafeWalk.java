package legitish.module.modules.player;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleDoubleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.CooldownUtils;
import legitish.utils.GameUtils;
import legitish.utils.MathUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
    public static ModuleTickSetting shift, shiftOnJump, blocksOnly, lookDown, onHold;
    public static ModuleDoubleSliderSetting pitchRange, shiftTime;
    private static boolean shouldBridge = false;
    private static boolean isShifting = false;
    private final CooldownUtils shiftTimer = new CooldownUtils(0);

    public SafeWalk() {
        super("Safe Walk", Module.category.Player, 0);
        this.registerSetting(shift = new ModuleTickSetting("Shift", false));
        this.registerSetting(shiftOnJump = new ModuleTickSetting("Shift on jump", false));
        this.registerSetting(shiftTime = new ModuleDoubleSliderSetting("Shift time", 125.0D, 175.0D, 0.0D, 300.0D, 5.0D));
        this.registerSetting(blocksOnly = new ModuleTickSetting("Blocks only", true));
        this.registerSetting(onHold = new ModuleTickSetting("On shift hold", false));
        this.registerSetting(lookDown = new ModuleTickSetting("Only when looking down", true));
        this.registerSetting(pitchRange = new ModuleDoubleSliderSetting("Pitch min range:", 70D, 85D, 0D, 90D, 1D));
    }

    public void onDisable() {
        if (shift.isToggled() && GameUtils.playerAboveAir()) {
            this.setShift(false);
        }

        shouldBridge = false;
        isShifting = false;
    }

    public void update() {
        if (!GameUtils.isPlayerInGame()) {
            return;
        }

        if (shift.isToggled()) {
            if (lookDown.isToggled()) {
                if (mc.thePlayer.rotationPitch < pitchRange.getInputMin() || mc.thePlayer.rotationPitch > pitchRange.getInputMax()) {
                    shouldBridge = false;
                    if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                        setShift(true);
                    }
                    return;
                }
            }
            if (onHold.isToggled()) {
                if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                    shouldBridge = false;
                    return;
                }
            }

            if (blocksOnly.isToggled()) {
                ItemStack i = mc.thePlayer.getHeldItem();
                if (i == null || !(i.getItem() instanceof ItemBlock)) {
                    if (isShifting) {
                        isShifting = false;
                        this.setShift(false);
                    }

                    return;
                }
            }

            if (mc.thePlayer.onGround) {
                if (GameUtils.playerAboveAir()) {
                    shiftTimer.setCooldown(MathUtils.randomInt(shiftTime.getInputMin(), shiftTime.getInputMax() + 0.1));
                    shiftTimer.start();
                    isShifting = true;
                    this.setShift(true);
                    shouldBridge = true;
                } else if (mc.thePlayer.isSneaking() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && onHold.isToggled()) {
                    isShifting = false;
                    shouldBridge = false;
                    this.setShift(false);
                } else if (onHold.isToggled() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                    isShifting = false;
                    shouldBridge = false;
                    this.setShift(false);
                } else if (mc.thePlayer.isSneaking() && (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && onHold.isToggled()) && (shiftTimer.hasFinished())) {
                    isShifting = false;
                    this.setShift(false);
                    shouldBridge = true;
                } else if (mc.thePlayer.isSneaking() && !onHold.isToggled() && (shiftTimer.hasFinished())) {
                    isShifting = false;
                    this.setShift(false);
                    shouldBridge = true;
                }
            } else if (shouldBridge && mc.thePlayer.capabilities.isFlying) {
                this.setShift(false);
                shouldBridge = false;
            } else if (shouldBridge && GameUtils.playerAboveAir() && shiftOnJump.isToggled()) {
                isShifting = true;
                this.setShift(true);
            } else {
                isShifting = false;
                this.setShift(false);
            }
        }
    }

    private void setShift(boolean shift) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), shift);
    }
}
