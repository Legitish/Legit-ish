package legitish.module.modules.movement;

import legitish.events.Subscribe;
import legitish.events.impl.LivingUpdateEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.utils.GameUtils;

public class NoJumpDelay extends Module {
    public NoJumpDelay() {
        super("NoJumpDelay", category.Movement, 0);
        this.registerSetting(new ModuleDesc("Removes the delay between jumps."));
    }

    @Subscribe(eventClass = LivingUpdateEvent.class)
    public void onLivingUpdate(LivingUpdateEvent e) {
        if (GameUtils.isPlayerInGame()) {
            mc.thePlayer.jumpTicks = 1;
        }
    }
}
