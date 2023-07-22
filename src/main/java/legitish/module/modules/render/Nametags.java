package legitish.module.modules.render;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
    public static ModuleSliderSetting offset;
    public static ModuleTickSetting rect, showHealth, showInvis, removeTags, e, noScale;

    public Nametags() {
        super("Nametags", Module.category.Visual, 0);
        this.registerSetting(offset = new ModuleSliderSetting("Offset", 0.0D, -40.0D, 40.0D, 1.0D));
        this.registerSetting(rect = new ModuleTickSetting("Rect", true));
        this.registerSetting(showHealth = new ModuleTickSetting("Show health", true));
        this.registerSetting(showInvis = new ModuleTickSetting("Show invis", true));
        this.registerSetting(noScale = new ModuleTickSetting("Don't scale", false));
        this.registerSetting(removeTags = new ModuleTickSetting("Remove tags", false));
    }

    @SubscribeEvent
    public void r(RenderLivingEvent.Pre e) {
        if (removeTags.isToggled()) {
            e.setCancelled(true);
        } else {
            if (e.getEntity() instanceof EntityPlayer && e.getEntity() != mc.thePlayer && e.getEntity().deathTime == 0) {
                EntityPlayer en = (EntityPlayer) e.getEntity();
                if (!showInvis.isToggled() && en.isInvisible()) {
                    return;
                }

                if (Targets.bot(en) || en.getDisplayName().getUnformattedText().isEmpty()) {
                    return;
                }

                //e.setCancelled(true);
                String str = en.getDisplayName().getFormattedText();
                if (showHealth.isToggled()) {
                    double r = en.getHealth() / en.getMaxHealth();
                    String h = (r < 0.3D ? "§c" : (r < 0.5D ? "§6" : (r < 0.7D ? "§e" : "§a"))) + MathUtils.round(en.getHealth(), 1);
                    str = str + " " + h;
                }

                GlStateManager.pushMatrix();
                GlStateManager.translate((float) e.getX() + 0.0F, (float) e.getY() + en.height + 0.5F, (float) e.getZ());
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
                float scale = (float) (0.02666667 * (noScale.isToggled() ? Math.sqrt(mc.thePlayer.getDistanceToEntity(e.getEntity())) * 0.5 : 1));
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
