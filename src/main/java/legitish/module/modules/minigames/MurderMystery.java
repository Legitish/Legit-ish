package legitish.module.modules.minigames;

import legitish.events.Subscribe;
import legitish.events.impl.RenderWorldEvent;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modules.client.Notifications;
import legitish.module.modules.client.Targets;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MurderMystery extends Module {
    private static final List<EntityPlayer> murderers = new ArrayList<>();
    private static final List<EntityPlayer> detectives = new ArrayList<>();
    public static ModuleTickSetting alert, searchDetectives, announceMurderer;

    public MurderMystery() {
        super("Murder Mystery", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Detects murderers and detectives in Murder Mystery."));
        this.registerSetting(alert = new ModuleTickSetting("Alert", true));
        this.registerSetting(searchDetectives = new ModuleTickSetting("Search detectives", true));
        this.registerSetting(announceMurderer = new ModuleTickSetting("Announce murderer", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderWorldEvent.class)
    public void findPlayers(RenderWorldEvent renderWorldEvent) {
        if (GameUtils.isPlayerInGame()) {
            if (ModuleManager.playerESP.isEnabled()) {
                ModuleManager.playerESP.disable();
            }

            if (!this.isMurderMystery()) {
                murderers.clear();
                detectives.clear();
            } else {
                for (EntityPlayer en : mc.theWorld.playerEntities) {
                    if (!Targets.isTarget(en)) {
                        continue;
                    } else if (en.isInvisible()) {
                        continue;
                    }
                    if (en.getHeldItem() != null && en.getHeldItem().hasDisplayName()) {
                        Item i = en.getHeldItem().getItem();
                        if (i instanceof ItemSword || i instanceof ItemAxe || en.getHeldItem().getDisplayName().contains("aKnife")) {
                            if (!murderers.contains(en)) {
                                murderers.add(en);
                                if (alert.isToggled()) {
                                    mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                                    Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " is a murderer!", 3000);
                                }

                                if (announceMurderer.isToggled()) {
                                    mc.thePlayer.sendChatMessage(en.getName() + " is a murderer!");
                                }
                            }
                        } else if (i instanceof ItemBow && searchDetectives.isToggled() && !detectives.contains(en)) {
                            detectives.add(en);
                            if (alert.isToggled()) {
                                Notifications.sendNotification(Notifications.NotificationTypes.WARN, en.getName() + " has a bow!", 3000);
                            }

                            if (announceMurderer.isToggled()) {
                                mc.thePlayer.sendChatMessage(en.getName() + " has a bow!");
                            }
                        }
                    }

                    int rgb = Color.green.getRGB();
                    if (murderers.contains(en)) {
                        rgb = Color.red.getRGB();
                    } else if (detectives.contains(en)) {
                        rgb = Color.cyan.getRGB();
                    }

                    GLUtils.RenderESP(en, GLUtils.ESPTypes.ARROW, 0.0D, 0.0D, rgb, false);
                }
            }
        }
    }

    private boolean isMurderMystery() {
        if (mc.thePlayer.getWorldScoreboard() == null || mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1) == null) {
            return false;
        }

        String d = mc.thePlayer.getWorldScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
        if (!d.contains("MURDER") && !d.contains("MYSTERY")) {
            return false;
        }

        for (String l : GameUtils.getScoreboard()) {
            String s = MouseUtils.str(l);
            if (s.contains("Role:")) {
                return true;
            }
        }

        return false;
    }
}
