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
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BedwarsAlerts extends Module {
    public static ModuleTickSetting sound, fireball, diaSword, invis;
    public final HashMap<EntityPlayer, Item> players = new HashMap<>();

    public BedwarsAlerts() {
        super("Bedwars Alerts", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Alerts of certain events in Bedwars."));
        this.registerSetting(sound = new ModuleTickSetting("Play sound", true));
        this.registerSetting(new ModuleDesc("Alert when player holding"));
        this.registerSetting(fireball = new ModuleTickSetting("Fireball", true));
        this.registerSetting(diaSword = new ModuleTickSetting("Invis", true));
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
                if (players.get(en) != item) {
                    if (Item.getIdFromItem(item) == 385 && fireball.isToggled()) {
                        Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " is holding a Fireball! [" + Math.round(en.getDistanceToEntity(mc.thePlayer)) + "m]", 5000);
                        mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                    } else if (Item.getIdFromItem(item) == 276 && diaSword.isToggled()) {
                        Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " is holding a Diamond Sword! [" + Math.round(en.getDistanceToEntity(mc.thePlayer)) + "m]" + item.getUnlocalizedName(), 5000);
                        mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                    } else if (item instanceof ItemPotion) {
                        if (mc.thePlayer.getHeldItem().getItemDamage() == 8206 && invis.isToggled()) {
                            Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " is holding a Diamond Sword! [" + Math.round(en.getDistanceToEntity(mc.thePlayer)) + "m]" + item.getUnlocalizedName(), 5000);
                            mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                        }
                    }
                    players.remove(en);
                }
                players.put(en, en.getHeldItem().getItem());
            }
        } else {
            players.clear();
        }
    }
}
