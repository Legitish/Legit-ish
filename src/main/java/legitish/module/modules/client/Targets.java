package legitish.module.modules.client;

import legitish.module.Module;
import legitish.module.modules.player.Freecam;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Targets extends Module {
    public static ModuleTickSetting bots, removeDead;

    public Targets() {
        super("Targets", category.Client, 0);
        this.registerSetting(new ModuleDesc("Sets targets for certain modules."));
        this.registerSetting(bots = new ModuleTickSetting("Don't target bots", true));
        this.registerSetting(removeDead = new ModuleTickSetting("Remove dead", true));
    }

    public static boolean isTarget(EntityPlayer en) {
        if (Freecam.en != null && Freecam.en == en) {
            return false;
        } else if (en == mc.thePlayer) {
            return false;
        } else if (en.deathTime != 0) {
            return false;
        } else if (en.isDead && removeDead.isToggled()) {
            return false;
        } else if (bots.isToggled()) {
            NetworkPlayerInfo info = mc.getNetHandler().getPlayerInfo(en.getUniqueID());
            return info != null;
        }
        return true;
    }
}
