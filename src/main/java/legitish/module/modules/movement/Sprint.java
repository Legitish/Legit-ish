package legitish.module.modules.movement;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

public class Sprint extends Module {
    public static ModuleTickSetting omniSprint;

    public Sprint() {
        super("Sprint", Module.category.Movement, 0);
        this.registerSetting(omniSprint = new ModuleTickSetting("OmniSprint", false));
    }

    @SubscribeEvent
    public void onTick(TickEvent tickEvent) {
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
