package legitish.module.modules.render;

import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modules.player.Freecam;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.ColorUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

import java.util.HashMap;

public class Targets extends Module {
    private static final HashMap<EntityPlayer, Long> newEnt = new HashMap<>();
    public static ModuleTickSetting cooldown;
    public static ModuleTickSetting removeDead;

    public Targets() {
        super("AntiBot", Module.category.Visual, 0);
        this.registerSetting(cooldown = new ModuleTickSetting("Wait 80 ticks", false));
        this.registerSetting(removeDead = new ModuleTickSetting("Remove dead", true));
    }

    public static boolean bot(EntityPlayer en) {
        if (Freecam.en != null && Freecam.en == en) {
            return true;
        } else if (!ModuleManager.antiBot.isEnabled()) {
            return false;
        } else if (cooldown.isToggled() && !newEnt.isEmpty() && newEnt.containsKey(en)) {
            return true;
        } else if (en.getName().startsWith("§c")) {
            return true;
        } else if (en.isDead && removeDead.isToggled()) {
            return true;
        } else return ColorUtils.stripColor(en.getDisplayName().getFormattedText()).contains("[NPC]");
    }

    public void onDisable() {
        newEnt.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent tickEvent) {
        if (cooldown.isToggled() && !newEnt.isEmpty()) {
            long now = System.currentTimeMillis();
            newEnt.values().removeIf(e -> e < now - 4000L);
        }
    }
}
