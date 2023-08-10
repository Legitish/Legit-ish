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
import net.minecraft.entity.player.EntityPlayer;

import java.awt.*;
import java.util.Iterator;

public class Tracers extends Module {
    public static ModuleTickSetting a;
    public static ModuleSliderSetting b;
    public static ModuleSliderSetting c;
    public static ModuleSliderSetting d;
    public static ModuleSliderSetting f;

    public Tracers() {
        super("Tracers", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Draws a line to enemies."));
        this.registerSetting(a = new ModuleTickSetting("Show invis", true));
        this.registerSetting(f = new ModuleSliderSetting("Line Width", 1.0D, 1.0D, 5.0D, 1.0D));
        this.registerSetting(b = new ModuleSliderSetting("Red", 0.0D, 0.0D, 255.0D, 1.0D));
        this.registerSetting(c = new ModuleSliderSetting("Green", 255.0D, 0.0D, 255.0D, 1.0D));
        this.registerSetting(d = new ModuleSliderSetting("Blue", 0.0D, 0.0D, 255.0D, 1.0D));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderWorldEvent.class)
    public void findTargets(RenderWorldEvent event) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = (new Color((int) b.getInput(), (int) c.getInput(), (int) d.getInput())).getRGB();
            for (EntityPlayer en : mc.theWorld.playerEntities) {
                if (!Targets.isTarget(en)) {
                    continue;
                } else if (!a.isToggled() && en.isInvisible()) {
                    continue;
                }
                GLUtils.drawTracerLine(en, rgb, (float) f.getInput());
            }
        }
    }
}
