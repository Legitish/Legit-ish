package legitish.module.modules.render;

import legitish.events.Subscribe;
import legitish.events.impl.RenderWorldEvent;
import legitish.module.Module;
import legitish.module.modules.client.Targets;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.Iterator;

@SuppressWarnings("unused")
public class PlayerESP extends Module {
    public static ModuleDesc desc;
    public static ModuleSliderSetting i;
    public static ModuleSliderSetting j;
    public static ModuleTickSetting f;
    public static ModuleTickSetting h;
    public static ModuleTickSetting box3d;
    public static ModuleTickSetting shaded;
    public static ModuleTickSetting box2d;
    public static ModuleTickSetting health;
    private int rgb_c = 0;

    public PlayerESP() {
        super("Player ESP", category.Visual, 0);
        this.registerSetting(desc = new ModuleDesc("Renders an overlay over players."));
        this.registerSetting(box2d = new ModuleTickSetting("2D", false));
        this.registerSetting(box3d = new ModuleTickSetting("Box", false));
        this.registerSetting(health = new ModuleTickSetting("Health", true));
        this.registerSetting(shaded = new ModuleTickSetting("Shaded", false));
        this.registerSetting(i = new ModuleSliderSetting("Expand", 0.0D, -0.3D, 2.0D, 0.1D));
        this.registerSetting(j = new ModuleSliderSetting("X-Shift", 0.0D, -35.0D, 10.0D, 1.0D));
        this.registerSetting(f = new ModuleTickSetting("Show invis", true));
        this.registerSetting(h = new ModuleTickSetting("Red on damage", true));
    }

    public void guiUpdate() {
        this.rgb_c = (new Color(0, 255, 0).getRGB());
    }

    @Subscribe(eventClass = RenderWorldEvent.class)
    public void findTargets(RenderWorldEvent renderWorldEvent) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = this.rgb_c;
            Iterator<EntityPlayer> var3 = mc.theWorld.playerEntities.iterator();

            while (true) {
                EntityPlayer en;
                do {
                    do {
                        do {
                            if (!var3.hasNext()) {
                                return;
                            }

                            en = var3.next();
                        } while (en == mc.thePlayer);
                    } while (en.deathTime != 0);
                } while (!f.isToggled() && en.isInvisible());

                if (!Targets.bot(en)) {
                    this.renderESP(en, rgb);
                }
            }
        }
    }

    private void renderESP(Entity en, int rgb) {
        if (box3d.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.BOX3D, i.getInput(), j.getInput(), rgb, h.isToggled());
        }

        if (shaded.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.SHADED, i.getInput(), j.getInput(), rgb, h.isToggled());
        }

        if (box2d.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.BOX2D, i.getInput(), j.getInput(), rgb, h.isToggled());
        }

        if (health.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.HEALTH, i.getInput(), j.getInput(), rgb, h.isToggled());
        }
    }
}
