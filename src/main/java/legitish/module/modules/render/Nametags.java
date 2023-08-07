package legitish.module.modules.render;

import legitish.events.Subscribe;
import legitish.events.impl.RenderNameplateEvent;
import legitish.module.Module;
import legitish.module.modules.client.Targets;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
    public static ModuleDesc desc;
    public static ModuleSliderSetting offset;
    public static ModuleTickSetting rect, showHealth, showInvis, removeTags, noScale;

    public Nametags() {
        super("Nametags", category.Visual, 0);
        this.registerSetting(desc = new ModuleDesc("Improves vanilla nametags."));
        this.registerSetting(offset = new ModuleSliderSetting("Offset", 0.0D, -40.0D, 40.0D, 1.0D));
        this.registerSetting(rect = new ModuleTickSetting("Rect", true));
        this.registerSetting(showHealth = new ModuleTickSetting("Show health", true));
        this.registerSetting(showInvis = new ModuleTickSetting("Show invis", true));
        this.registerSetting(noScale = new ModuleTickSetting("Don't scale", false));
        this.registerSetting(removeTags = new ModuleTickSetting("Remove tags", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventType = RenderNameplateEvent.class)
    public void renderNameplates(RenderNameplateEvent event) {
        if (removeTags.isToggled()) {
            event.setCancelled(true);
        } else {
            if (event.entity instanceof EntityPlayer && event.entity != mc.thePlayer && event.entity.deathTime == 0) {
                EntityPlayer en = (EntityPlayer) event.entity;
                if (!showInvis.isToggled() && en.isInvisible()) {
                    return;
                }

                if (Targets.bot(en) || en.getDisplayName().getUnformattedText().isEmpty()) {
                    return;
                }

                event.setCancelled(true);
                String str = en.getDisplayName().getFormattedText();
                if (showHealth.isToggled()) {
                    double r = en.getHealth() / en.getMaxHealth();
                    String h = GameUtils.formatColorCode(r < 0.3D ? "&c" : (r < 0.5D ? "&6" : (r < 0.7D ? "&e" : "&a"))) + MathUtils.round(en.getHealth(), 1);
                    str = str + " " + h;
                }

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) event.x + 0.0F, (float) event.y + en.height + 0.5F, (float) event.z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
                float scale = (float) (0.02666667 * (noScale.isToggled() ? Math.sqrt(mc.thePlayer.getDistanceToEntity(event.entity)) * 0.5 : 1));
                GlStateManager.scale(-scale, -scale, scale);
                if (en.isSneaking()) {
                    GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                }

                GlStateManager.disableLighting();
                GlStateManager.depthMask(false);
                GlStateManager.disableDepth();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                int i = (int) (-offset.getInput());
                int j = mc.fontRendererObj.getStringWidth(str) / 2;
                GlStateManager.disableTexture2D();
                if (rect.isToggled()) {
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos(-j - 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos(-j - 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos(j + 1, 8 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    worldrenderer.pos(j + 1, -1 + i, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
                    tessellator.draw();
                }

                GlStateManager.enableTexture2D();
                mc.fontRendererObj.drawString(str, -mc.fontRendererObj.getStringWidth(str) / 2, i, -1);
                GlStateManager.enableDepth();
                GlStateManager.depthMask(true);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.popMatrix();
            }

        }
    }
}
