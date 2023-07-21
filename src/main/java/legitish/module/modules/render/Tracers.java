package legitish.module.modules.render;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.awt.*;
import java.util.Iterator;

public class Tracers extends Module {
    public static ModuleTickSetting a;
    public static ModuleSliderSetting b;
    public static ModuleSliderSetting c;
    public static ModuleSliderSetting d;
    public static ModuleTickSetting e;
    public static ModuleSliderSetting f;
    private boolean g;

    public Tracers() {
        super("Tracers", Module.category.Visual, 0);
        this.registerSetting(a = new ModuleTickSetting("Show invis", true));
        this.registerSetting(f = new ModuleSliderSetting("Line Width", 1.0D, 1.0D, 5.0D, 1.0D));
        this.registerSetting(b = new ModuleSliderSetting("Red", 0.0D, 0.0D, 255.0D, 1.0D));
        this.registerSetting(c = new ModuleSliderSetting("Green", 255.0D, 0.0D, 255.0D, 1.0D));
        this.registerSetting(d = new ModuleSliderSetting("Blue", 0.0D, 0.0D, 255.0D, 1.0D));
    }

    public void onEnable() {
        this.g = mc.gameSettings.viewBobbing;
        if (this.g) {
            mc.gameSettings.viewBobbing = false;
        }

    }

    public void onDisable() {
        mc.gameSettings.viewBobbing = this.g;
    }

    public void update() {
        if (mc.gameSettings.viewBobbing) {
            mc.gameSettings.viewBobbing = false;
        }

    }

    @SubscribeEvent
    public void o(RenderWorldEvent ev) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = (new Color((int) b.getInput(), (int) c.getInput(), (int) d.getInput())).getRGB();
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
                } while (!a.isToggled() && en.isInvisible());

                if (!AntiBot.bot(en)) {
                    GLUtils.drawTracerLine(en, rgb, (float) f.getInput());
                }
            }
        }
    }
}
