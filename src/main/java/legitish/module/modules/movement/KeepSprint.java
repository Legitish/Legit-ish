package legitish.module.modules.movement;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleDesc;
import legitish.module.ModuleManager;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import net.minecraft.entity.Entity;

public class KeepSprint extends Module {
    public static ModuleDesc moduleDesc;
    public static ModuleSliderSetting slow;
    public static ModuleTickSetting onlyReach;

    public KeepSprint() {
        super("Keep Sprint", category.Movement, 0);
        this.registerSetting(moduleDesc = new ModuleDesc("Default is 40% motion reduction."));
        this.registerSetting(slow = new ModuleSliderSetting("Slow %", 40.0D, 0.0D, 40.0D, 1.0D));
        this.registerSetting(onlyReach = new ModuleTickSetting("Only reduce reach hits", false));
    }

    public static void sl(Entity en) {
        double dist;
        if (onlyReach.isToggled() && ModuleManager.reach.isEnabled() && !mc.thePlayer.capabilities.isCreativeMode) {
            dist = mc.objectMouseOver.hitVec.distanceTo(mc.getRenderViewEntity().getPositionEyes(1.0F));
            double val;
            if (dist > 3.0D) {
                val = (100.0D - (double) ((float) slow.getInput())) / 100.0D;
            } else {
                val = 0.6D;
            }

            mc.thePlayer.motionX *= val;
            mc.thePlayer.motionZ *= val;
        } else {
            dist = (100.0D - (double) ((float) slow.getInput())) / 100.0D;
            mc.thePlayer.motionX *= dist;
            mc.thePlayer.motionZ *= dist;
        }
    }
}
