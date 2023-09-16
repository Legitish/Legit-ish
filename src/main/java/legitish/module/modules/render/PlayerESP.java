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

@SuppressWarnings("unused")
public class PlayerESP extends Module {
    public static ModuleSliderSetting expand, xshift;
    public static ModuleTickSetting showInvis, redOnDamage;
    public static ModuleTickSetting box2d, box3d, health, shaded;
    private int rgb_c = 0;

    public PlayerESP() {
        super("Player ESP", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Renders an overlay over players."));
        this.registerSetting(box2d = new ModuleTickSetting("2D", false));
        this.registerSetting(box3d = new ModuleTickSetting("Box", false));
        this.registerSetting(health = new ModuleTickSetting("Health", true));
        this.registerSetting(shaded = new ModuleTickSetting("Shaded", false));
        this.registerSetting(expand = new ModuleSliderSetting("Expand", 0.0D, -0.3D, 2.0D, 0.1D));
        this.registerSetting(xshift = new ModuleSliderSetting("X-Shift", 0.0D, -35.0D, 10.0D, 1.0D));
        this.registerSetting(showInvis = new ModuleTickSetting("Show invis", true));
        this.registerSetting(redOnDamage = new ModuleTickSetting("Red on damage", true));
    }

    public void guiUpdate() {
        this.rgb_c = (new Color(0, 255, 0).getRGB());
    }

    @Subscribe(eventClass = RenderWorldEvent.class)
    public void findTargets(RenderWorldEvent renderWorldEvent) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = this.rgb_c;

            for (EntityPlayer en : mc.theWorld.playerEntities) {
                if (!Targets.isTarget(en)) {
                    continue;
                } else if (!showInvis.isToggled() && en.isInvisible()) {
                    continue;
                }
                this.renderESP(en, rgb);
            }
        }
    }

    private void renderESP(Entity en, int rgb) {
        if (box3d.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.BOX3D, expand.getInput(), xshift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (shaded.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.SHADED, expand.getInput(), xshift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (box2d.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.BOX2D, expand.getInput(), xshift.getInput(), rgb, redOnDamage.isToggled());
        }

        if (health.isToggled()) {
            GLUtils.RenderESP(en, GLUtils.ESPTypes.HEALTH, expand.getInput(), xshift.getInput(), rgb, redOnDamage.isToggled());
        }
    }
}
