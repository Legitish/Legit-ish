package legitish.module.modules.render;

import legitish.events.Subscribe;
import legitish.events.impl.RenderGameOverlayEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MathUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public class Health extends Module {

    public static ModuleTickSetting shadow;

    public Health() {
        super("Health", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Displays your health."));
        this.registerSetting(shadow = new ModuleTickSetting("Shadow", true));

    }

    @Subscribe(eventClass = RenderGameOverlayEvent.class)
    public void onRenderOverlay(RenderGameOverlayEvent e) {
        if(GameUtils.isPlayerInGame()) {

            FontRenderer fr = mc.fontRendererObj;

            double healthValue = mc.thePlayer.getHealth() / mc.thePlayer.getMaxHealth();
            String health = GameUtils.formatColorCode(healthValue < 0.3D ? "&c" : (healthValue < 0.5D ? "&6" : (healthValue < 0.7D ? "&e" : "&a"))) + MathUtils.round(mc.thePlayer.getHealth(), 1);
            String redheart = GameUtils.formatColorCode("&c\u2764");
            double absorptionValue = MathUtils.round(mc.thePlayer.getAbsorptionAmount(), 1);
            String absorption = GameUtils.formatColorCode("&e" + absorptionValue);
            String goldheart = GameUtils.formatColorCode("&e\u2764");

            if(absorptionValue <= 0.0) {
                ScaledResolution sr = new ScaledResolution(mc);
                int x = sr.getScaledWidth() / 2 - fr.getStringWidth(health + redheart) / 2;
                int y = sr.getScaledHeight() / 2 + 20;

                if(shadow.isToggled()) {
                    fr.drawStringWithShadow(health + redheart, x, y, -1);
                } else {
                    fr.drawString(health + redheart, x, y, -1);
                }
            } else { // PLEASE SOMEONE TELL ME A BETTER WAY TO DO THIS, I SWEAR IT HAS TO EXIST
                ScaledResolution sr = new ScaledResolution(mc);
                int x = sr.getScaledWidth() / 2 - fr.getStringWidth(health + redheart + " " + absorption + goldheart) / 2;
                int y = sr.getScaledHeight() / 2 + 20;

                if(shadow.isToggled()) {
                    fr.drawStringWithShadow(health + redheart + " " + absorption + goldheart, x, y, -1);
                } else {
                    fr.drawString(health + redheart + " " + absorption + goldheart, x, y, -1);
                }
            }


        }
    }
}
