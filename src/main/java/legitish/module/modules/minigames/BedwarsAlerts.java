package legitish.module.modules.minigames;

import legitish.events.Subscribe;
import legitish.events.impl.PlayerTickEvent;
import legitish.module.Module;
import legitish.module.modules.client.Notifications;
import legitish.module.modules.client.Targets;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class BedwarsAlerts extends Module {
    public static final List<EntityPlayer> players = new ArrayList<>();
    public static ModuleTickSetting sound, fireball, diaSword, invis;

    public BedwarsAlerts() {
        super("Bedwars Alerts", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Alerts of certain events in Bedwars."));
        this.registerSetting(sound = new ModuleTickSetting("Play sound", true));
        this.registerSetting(new ModuleDesc("Alert when player holding"));
        this.registerSetting(fireball = new ModuleTickSetting("Fireball", true));
        this.registerSetting(invis = new ModuleTickSetting("Invis", true));
        this.registerSetting(diaSword = new ModuleTickSetting("Diamond Sword", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = PlayerTickEvent.class)
    public void onTick(PlayerTickEvent event) {
        if (GameUtils.isPlayerInGame() && GameUtils.isBedwars()) {
            for (EntityPlayer en : mc.theWorld.playerEntities) {
                if (!Targets.isTarget(en)) {
                    continue;
                }
                Item item = en.getHeldItem().getItem();
                if (players.get(players.indexOf(en)) != en) {
                    if (Item.getIdFromItem(item) == 385 && fireball.isToggled()) {
                        Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " is holding a Fireball! [" + Math.round(en.getDistanceToEntity(mc.thePlayer)) + "m]", 5000);
                        mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                    } else if (Item.getIdFromItem(item) == 276 && diaSword.isToggled()) {
                        Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " is holding a Diamond Sword! [" + Math.round(en.getDistanceToEntity(mc.thePlayer)) + "m]", 5000);
                        mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                    } else if (!players.contains(en) || players.get(players.indexOf(en)).isInvisible() != en.isInvisible()) {
                        Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " is invisible! [" + Math.round(en.getDistanceToEntity(mc.thePlayer)) + "m]", 5000);
                        mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                    }
                }
                players.remove(en);
                players.add(en);
            }
        } else {
            players.clear();
        }
    }
}
