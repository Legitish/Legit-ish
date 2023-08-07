package legitish.module.modules.player;

import legitish.events.Subscribe;
import legitish.events.impl.ClientTickEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.client.settings.KeyBinding;

public class AutoJump extends Module {
    public static ModuleDesc desc;
    public static ModuleTickSetting cancelShift;
    private boolean jumping = false;

    public AutoJump() {
        super("Auto Jump", category.Player, 0);
        this.registerSetting(desc = new ModuleDesc("Jumps at the edge of a block."));
        this.registerSetting(cancelShift = new ModuleTickSetting("Cancel when shifting", true));
    }

    public void onDisable() {
        this.jump(this.jumping = false);
    }

    @SuppressWarnings("unused")
    @Subscribe(eventType = ClientTickEvent.class)
    public void onTick(ClientTickEvent tickEvent) {
        if (GameUtils.isPlayerInGame()) {
            if (mc.thePlayer.onGround && (!cancelShift.isToggled() || !mc.thePlayer.isSneaking())) {
                if (mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(mc.thePlayer.motionX / 3.0D, -1.0D, mc.thePlayer.motionZ / 3.0D)).isEmpty()) {
                    this.jump(this.jumping = true);
                } else if (this.jumping) {
                    this.jump(this.jumping = false);
                }
            } else if (this.jumping) {
                this.jump(this.jumping = false);
            }

        }
    }

    private void jump(boolean jump) {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), jump);
    }
}
