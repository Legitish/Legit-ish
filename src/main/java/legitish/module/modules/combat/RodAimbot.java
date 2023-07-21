package legitish.module.modules.combat;

import legitish.module.Module;
import legitish.module.modules.render.AntiBot;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.util.Iterator;

public class RodAimbot extends Module {
    public static ModuleSliderSetting fov, distance;
    public static ModuleTickSetting aimInvis;

    public RodAimbot() {
        super("Rod Aimbot", Module.category.Combat, 0);
        this.registerSetting(fov = new ModuleSliderSetting("FOV", 90.0D, 15.0D, 360.0D, 1.0D));
        this.registerSetting(distance = new ModuleSliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
        this.registerSetting(aimInvis = new ModuleTickSetting("Aim invis", false));
    }

    @SubscribeEvent
    public void x(MouseEvent ev) {
        if (ev.getButton() == 1 && ev.getButtonState() && GameUtils.isPlayerInGame() && mc.currentScreen == null) {
            if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemFishingRod && mc.thePlayer.fishEntity == null) {
                Entity en = this.gE();
                if (en != null) {
                    ev.setCancelled(true);
                    GameUtils.aim(en, -7.0F, true);
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                }
            }

        }
    }

    public Entity gE() {
        int f = (int) fov.getInput();
        Iterator<EntityPlayer> var2 = mc.theWorld.playerEntities.iterator();

        EntityPlayer en;
        do {
            do {
                do {
                    do {
                        if (!var2.hasNext()) {
                            return null;
                        }

                        en = var2.next();
                    } while (en == mc.thePlayer);
                } while (en.deathTime != 0);
            } while (!aimInvis.isToggled() && en.isInvisible());
        } while ((double) mc.thePlayer.getDistanceToEntity(en) > distance.getInput() || AntiBot.bot(en) || !GameUtils.fov(en, (float) f));

        return en;
    }
}
