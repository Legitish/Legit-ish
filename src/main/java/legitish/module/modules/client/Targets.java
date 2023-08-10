package legitish.module.modules.client;

import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modules.player.Freecam;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

public class Targets extends Module {
    public static ModuleTickSetting bots, removeDead;

    public Targets() {
        super("Targets", category.Client, 0);
        this.registerSetting(new ModuleDesc("Sets targets for certain modules."));
        this.registerSetting(bots = new ModuleTickSetting("Don't target bots", true));
        this.registerSetting(removeDead = new ModuleTickSetting("Remove dead", true));
    }

    public static boolean bot(EntityPlayer en) {
        if (Freecam.en != null && Freecam.en == en) {
            return true;
        } else if (!ModuleManager.targets.isEnabled() || !bots.isToggled()) {
            return false;
        } else if (en.getName().startsWith("§c")) {
            return true;
        } else if (en.isDead && removeDead.isToggled()) {
            return true;
        } else if (bots.isToggled()) {
            NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(en.getUniqueID());
            return info == null;
        }
        return false;
    }
}
