package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.LivingUpdateEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public static ModuleSliderSetting horizontal, vertical, chance, tickDelay;
    public static ModuleTickSetting onlyWhileTargeting, holdingS;

    public Velocity() {
        super("Velocity", category.Combat, 0);
        this.registerSetting(new ModuleDesc("Modifies your knockback."));
        this.registerSetting(horizontal = new ModuleSliderSetting("Horizontal", 90.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(vertical = new ModuleSliderSetting("Vertical", 100.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(chance = new ModuleSliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(tickDelay = new ModuleSliderSetting("Tick Delay", 0.0D, 0.0D, 10.0D, 1.0D));
        this.registerSetting(onlyWhileTargeting = new ModuleTickSetting("Only while targeting", false));
        this.registerSetting(holdingS = new ModuleTickSetting("Disable while holding S", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = LivingUpdateEvent.class)
    public void onTick(LivingUpdateEvent event) {
        if (GameUtils.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime - tickDelay.getInput() && event.type == LivingUpdateEvent.Type.PRE) {
            if (onlyWhileTargeting.isToggled() && (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null)) {
                return;
            }

            if (holdingS.isToggled() && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                return;
            }

            if (chance.getInput() != 100.0D) {
                double ch = Math.random();
                if (ch >= chance.getInput() / 100.0D) {
                    return;
                }
            }

            if (horizontal.getInput() != 100.0D) {
                mc.thePlayer.motionX *= horizontal.getInput() / 100.0D;
                mc.thePlayer.motionZ *= horizontal.getInput() / 100.0D;
            }

            if (vertical.getInput() != 100.0D) {
                mc.thePlayer.motionY *= vertical.getInput() / 100.0D;
            }
        }
    }
}
