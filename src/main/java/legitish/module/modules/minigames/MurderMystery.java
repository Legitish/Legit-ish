package legitish.module.modules.minigames;

import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modules.render.Targets;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MurderMystery extends Module {
    private static final List<EntityPlayer> murderers = new ArrayList<>();
    private static final List<EntityPlayer> detectives = new ArrayList<>();
    public static ModuleTickSetting alert, searchDetectives, announceMurderer;

    public MurderMystery() {
        super("Murder Mystery", category.Visual, 0);
        this.registerSetting(alert = new ModuleTickSetting("Alert", true));
        this.registerSetting(searchDetectives = new ModuleTickSetting("Search detectives", true));
        this.registerSetting(announceMurderer = new ModuleTickSetting("Announce murderer", false));
    }

    @SubscribeEvent
    public void o(RenderWorldEvent renderWorldEvent) {
        if (GameUtils.isPlayerInGame()) {
            if (ModuleManager.playerESP.isEnabled()) {
                ModuleManager.playerESP.disable();
            }

            if (!this.isMurderMystery()) {
                this.c();
            } else {
                Iterator<EntityPlayer> var2 = mc.theWorld.playerEntities.iterator();

                while (true) {
                    EntityPlayer en;
                    do {
                        do {
                            do {
                                if (!var2.hasNext()) {
                                    return;
                                }

                                en = var2.next();
                            } while (en == mc.thePlayer);
                        } while (en.isInvisible());
                    } while (Targets.bot(en));

                    if (en.getHeldItem() != null && en.getHeldItem().hasDisplayName()) {
                        Item i = en.getHeldItem().getItem();
                        if (i instanceof ItemSword || i instanceof ItemAxe || en.getHeldItem().getDisplayName().contains("aKnife")) {
                            if (!murderers.contains(en)) {
                                murderers.add(en);
                                if (alert.isToggled()) {
                                    mc.thePlayer.playSound("note.pling", 1.0F, 1.0F);
                                    GameUtils.sendChat(en.getName() + " is a murderer!");
                                }

                                if (announceMurderer.isToggled()) {
                                    mc.thePlayer.sendChatMessage(en.getName() + " is a murderer!");
                                }
                            }
                        } else if (i instanceof ItemBow && searchDetectives.isToggled() && !detectives.contains(en)) {
                            detectives.add(en);
                            if (alert.isToggled()) {
                                GameUtils.sendChat(en.getName() + " has a bow!");
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

                    GLUtils.RenderESP(en, 5, 0.0D, 0.0D, rgb, false);
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

    private void c() {
        murderers.clear();
        detectives.clear();
    }
}
