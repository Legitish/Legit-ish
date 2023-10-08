package legitish.module.modules.movement;

import legitish.events.Subscribe;
import legitish.module.Module;
import legitish.events.impl.LivingUpdateEvent;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.utils.GameUtils;

public class InstantStop extends Module {
    private int onGroundTicks;

    public InstantStop() {
        super("InstantStop", category.Movement, 0);
        this.registerSetting(new ModuleDesc("Instantly stop moving when you let go of movement keys."));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = LivingUpdateEvent.class)
    public void onUpdate(LivingUpdateEvent e) {
        if (GameUtils.isPlayerInGame()) {
            if (mc.thePlayer.onGround){
                onGroundTicks++;
            } else {
                onGroundTicks = 0;
            }

            if (mc.thePlayer.onGround && onGroundTicks >= 5
                    && mc.thePlayer.movementInput.moveForward == 0F
                    && mc.thePlayer.movementInput.moveStrafe == 0F
                    && mc.thePlayer.hurtTime < 1) {
                mc.thePlayer.motionX = 0.0;
                mc.thePlayer.motionZ = 0.0;
            }
        }
    }
}