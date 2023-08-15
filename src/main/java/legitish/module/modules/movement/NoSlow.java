package legitish.module.modules.movement;

import legitish.events.Subscribe;
import legitish.events.impl.SlowdownEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;

public class NoSlow extends Module {
    public static ModuleSliderSetting b;

    public NoSlow() {
        super("NoSlow", category.Movement, 0);
        this.registerSetting(new ModuleDesc("Reduces slowdown from certain actions."));
        this.registerSetting(b = new ModuleSliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = SlowdownEvent.class)
    public static void noSlow(SlowdownEvent event) {
        event.setCancelled(true);
        float val = (100.0F - (float) b.getInput()) / 100.0F;
        mc.thePlayer.movementInput.moveStrafe *= val;
        mc.thePlayer.movementInput.moveForward *= val;
    }
}
