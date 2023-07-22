package legitish.module.modules.render;

import legitish.module.Module;
import legitish.module.ModuleDesc;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;
import java.util.Iterator;

public class PlayerESP extends Module {
    public static ModuleDesc g;
    public static ModuleSliderSetting a;
    public static ModuleSliderSetting i;
    public static ModuleSliderSetting j;
    public static ModuleTickSetting f;
    public static ModuleTickSetting h;
    public static ModuleTickSetting t1;
    public static ModuleTickSetting t2;
    public static ModuleTickSetting t3;
    public static ModuleTickSetting t4;
    private int rgb_c = 0;

    public PlayerESP() {
        super("Player ESP", Module.category.Visual, 0);

        this.registerSetting(g = new ModuleDesc("ESP Types"));
        this.registerSetting(t3 = new ModuleTickSetting("2D", false));
        this.registerSetting(t1 = new ModuleTickSetting("Box", false));
        this.registerSetting(t4 = new ModuleTickSetting("Health", true));
        this.registerSetting(t2 = new ModuleTickSetting("Shaded", false));
        this.registerSetting(i = new ModuleSliderSetting("Expand", 0.0D, -0.3D, 2.0D, 0.1D));
        this.registerSetting(j = new ModuleSliderSetting("X-Shift", 0.0D, -35.0D, 10.0D, 1.0D));
        this.registerSetting(f = new ModuleTickSetting("Show invis", true));
        this.registerSetting(h = new ModuleTickSetting("Red on damage", true));
    }

    public void guiUpdate() {
        this.rgb_c = (new Color(0, 255, 0).getRGB());
    }

    @SubscribeEvent
    public void r1(RenderWorldEvent renderWorldEvent) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = this.rgb_c;
            Iterator var3;
            var3 = mc.theWorld.playerEntities.iterator();

            while (true) {
                EntityPlayer en;
                do {
                    do {
                        do {
                            if (!var3.hasNext()) {
                                return;
                            }

                            en = (EntityPlayer) var3.next();
                        } while (en == mc.thePlayer);
                    } while (en.deathTime != 0);
                } while (!f.isToggled() && en.isInvisible());

                if (!Targets.bot(en)) {
                    this.r(en, rgb);
                }
            }
        }
    }

    private void r(Entity en, int rgb) {
        if (t1.isToggled()) {
            GLUtils.RenderESP(en, 1, i.getInput(), j.getInput(), rgb, h.isToggled());
        }

        if (t2.isToggled()) {
            GLUtils.RenderESP(en, 2, i.getInput(), j.getInput(), rgb, h.isToggled());
        }

        if (t3.isToggled()) {
            GLUtils.RenderESP(en, 3, i.getInput(), j.getInput(), rgb, h.isToggled());
        }

        if (t4.isToggled()) {
            GLUtils.RenderESP(en, 4, i.getInput(), j.getInput(), rgb, h.isToggled());
        }
    }
}
