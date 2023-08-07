package legitish.module.modules.movement;

import legitish.events.Subscribe;
import legitish.events.impl.ClientTickEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;

public class Sprint extends Module {
    public static ModuleDesc desc;
    public static ModuleTickSetting omniSprint;

    public Sprint() {
        super("Sprint", category.Movement, 0);
        this.registerSetting(desc = new ModuleDesc("Sprints automatically."));
        this.registerSetting(omniSprint = new ModuleTickSetting("OmniSprint", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventType = ClientTickEvent.class)
    public void onTick(ClientTickEvent event) {
        if (GameUtils.isPlayerInGame() && mc.inGameHasFocus) {
            EntityPlayerSP p = mc.thePlayer;
            if (omniSprint.isToggled()) {
                if (GameUtils.isMoving() && p.getFoodStats().getFoodLevel() > 6) {
                    p.setSprinting(true);
                }
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }
        }

    }
}
