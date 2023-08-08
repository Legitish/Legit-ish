package legitish.module.modules.movement;

import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import org.lwjgl.input.Mouse;

public class NoSlow extends Module {
    public static ModuleDesc a;
    public static ModuleDesc c;
    public static ModuleSliderSetting b;

    public NoSlow() {
        super("NoSlow", category.Movement, 0);
        this.registerSetting(a = new ModuleDesc("Reduces slowdown from certain actions."));
        this.registerSetting(b = new ModuleSliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
    }

    public static void noSlow() {
       float val = (100.0F - (float) b.getInput()) / 100.0F;
       mc.thePlayer.movementInput.moveStrafe *= val;
       mc.thePlayer.movementInput.moveForward *= val;
    }
}
