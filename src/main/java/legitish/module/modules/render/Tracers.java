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

public class Tracers extends Module {
    public static ModuleTickSetting showInvis;
    public static ModuleSliderSetting lineWidth, red, green, blue;

    public Tracers() {
        super("Tracers", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Draws a line to enemies."));
        this.registerSetting(showInvis = new ModuleTickSetting("Show invis", true));
        this.registerSetting(lineWidth = new ModuleSliderSetting("Line Width", 1.0D, 1.0D, 5.0D, 1.0D));
        this.registerSetting(red = new ModuleSliderSetting("Red", 0.0D, 0.0D, 255.0D, 1.0D));
        this.registerSetting(green = new ModuleSliderSetting("Green", 255.0D, 0.0D, 255.0D, 1.0D));
        this.registerSetting(blue = new ModuleSliderSetting("Blue", 0.0D, 0.0D, 255.0D, 1.0D));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderWorldEvent.class)
    public void findTargets(RenderWorldEvent event) {
        if (GameUtils.isPlayerInGame()) {
            int rgb = (new Color((int) red.getInput(), (int) green.getInput(), (int) blue.getInput())).getRGB();
            for (EntityPlayer en : mc.theWorld.playerEntities) {
                if (!Targets.isTarget(en)) {
                    continue;
                } else if (!showInvis.isToggled() && en.isInvisible()) {
                    continue;
                }
                GLUtils.drawTracerLine(en, rgb, (float) lineWidth.getInput());
            }
        }
    }
}
