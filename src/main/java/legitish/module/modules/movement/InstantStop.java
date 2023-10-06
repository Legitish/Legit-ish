package legitish.module.modules.movement;

import legitish.events.Subscribe;
import legitish.module.Module;
import legitish.events.impl.LivingUpdateEvent;
import legitish.utils.GameUtils;

public class InstantStop extends Module {
    public InstantStop() {
        super("InstantStop", category.Movement, 0);
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = LivingUpdateEvent.class)
    public void onUpdate(LivingUpdateEvent e) {
        if (GameUtils.isPlayerInGame() && mc.thePlayer.onGround && mc.thePlayer.movementInput.moveForward == 0F && mc.thePlayer.movementInput.moveStrafe == 0F) {
            mc.thePlayer.motionX = 0.0;
            mc.thePlayer.motionZ = 0.0;
        }
    }
}