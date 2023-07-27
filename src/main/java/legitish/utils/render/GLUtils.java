package legitish.utils.render;

import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class GLUtils {
    // fixing this never >:(
    private static final Minecraft mc = Minecraft.getMinecraft();
    // Can someone explain to me the difference between putting this in a function vs putting it outside? I feel like it's
    // the updating but if it isnt then I cant be bothered to change it

    public static void HighlightBlock(BlockPos bp, int color, boolean shade) {
        if (bp != null) {
            double x = (double) bp.getX() - mc.getRenderManager().viewerPosX;
            double y = (double) bp.getY() - mc.getRenderManager().viewerPosY;
            double z = (double) bp.getZ() - mc.getRenderManager().viewerPosZ;
            glBlendFunc(770, 771);
            glEnable(3042);
            glLineWidth(2.0F);
            glDisable(3553);
            glDisable(2929);
            glDepthMask(false);
            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            glColor4d(r, g, b, a);
            RenderGlobal.drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D));
            if (shade) {
                drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0D, y + 1.0D, z + 1.0D), r, g, b);
            }

            glEnable(3553);
            glEnable(2929);
            glDepthMask(true);
            glDisable(3042);
        }
    }

    public static void RenderESP(Entity e, int type, double expand, double shift, int color, boolean damage) {
        if (e instanceof EntityLivingBase && MouseUtils.getTimer() != null) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) MouseUtils.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) MouseUtils.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) MouseUtils.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;
            float d = (float) expand / 40.0F;
            if (e instanceof EntityPlayer && damage && ((EntityPlayer) e).hurtTime != 0) {
                color = Color.RED.getRGB();
            }

            GlStateManager.pushMatrix();
            //2d
            if (type == 3) {
                glTranslated(x, y - 0.2D, z);
                glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                GlStateManager.disableDepth();
                glScalef(0.03F + d, 0.03F + d, 0.03F + d);
                int outline = Color.black.getRGB();
                net.minecraft.client.gui.Gui.drawRect(-20, -1, -26, 75, outline);
                net.minecraft.client.gui.Gui.drawRect(20, -1, 26, 75, outline);
                net.minecraft.client.gui.Gui.drawRect(-20, -1, 21, 5, outline);
                net.minecraft.client.gui.Gui.drawRect(-20, 70, 21, 75, outline);
                if (color != 0) {
                    net.minecraft.client.gui.Gui.drawRect(-21, 0, -25, 74, color);
                    net.minecraft.client.gui.Gui.drawRect(21, 0, 25, 74, color);
                    net.minecraft.client.gui.Gui.drawRect(-21, 0, 24, 4, color);
                    net.minecraft.client.gui.Gui.drawRect(-21, 71, 25, 74, color);
                } else {
                    int st = ColorUtils.getBackgroundColor(1).getRGB();
                    int en = ColorUtils.getBackgroundColor(2).getRGB();
                    drawGradient(-21, 0, -25, 74, st, en);
                    drawGradient(21, 0, 25, 74, st, en);
                    net.minecraft.client.gui.Gui.drawRect(-21, 0, 21, 4, en);
                    net.minecraft.client.gui.Gui.drawRect(-21, 71, 21, 74, st);
                }

                GlStateManager.enableDepth();
            } else {
                int i;
                if (type == 4) {
                    EntityLivingBase en = (EntityLivingBase) e;
                    double r = en.getHealth() / en.getMaxHealth();
                    int b = (int) (74.0D * r);
                    int hc = r < 0.3D ? Color.red.getRGB() : (r < 0.5D ? Color.orange.getRGB() : (r < 0.7D ? Color.yellow.getRGB() : Color.green.getRGB()));
                    glTranslated(x, y - 0.2D, z);
                    glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                    GlStateManager.disableDepth();
                    glScalef(0.0275F + d, 0.0275F + d, 0.0275F + d);
                    i = (int) (21.0D + shift * 2.0D);
                    net.minecraft.client.gui.Gui.drawRect(i, -1, i + 5, 75, Color.black.getRGB());
                    net.minecraft.client.gui.Gui.drawRect(i + 1, b, i + 4, 74, Color.darkGray.getRGB());
                    net.minecraft.client.gui.Gui.drawRect(i + 1, 0, i + 4, b, hc);
                    GlStateManager.enableDepth();
                } else {
                    if (color == 0) {
                        color = ColorUtils.getBackgroundColor(1).getRGB();
                    }

                    float a = (float) (color >> 24 & 255) / 255.0F;
                    float r = (float) (color >> 16 & 255) / 255.0F;
                    float g = (float) (color >> 8 & 255) / 255.0F;
                    float b = (float) (color & 255) / 255.0F;
                    // arrow
                    if (type == 5) {
                        glTranslated(x, y - 0.2D, z);
                        glRotated(-mc.getRenderManager().playerViewY, 0.0D, 1.0D, 0.0D);
                        GlStateManager.disableDepth();
                        glScalef(0.03F + d, 0.03F, 0.03F + d);
                        d2p(0.0D, 95.0D, 10, 3, Color.black.getRGB());

                        for (i = 0; i < 6; ++i) {
                            d2p(0.0D, 95 + (10 - i), 3, 4, Color.black.getRGB());
                        }

                        for (i = 0; i < 7; ++i) {
                            d2p(0.0D, 95 + (10 - i), 2, 4, color);
                        }

                        d2p(0.0D, 95.0D, 8, 3, color);
                        GlStateManager.enableDepth();
                    } else {
                        AxisAlignedBB bbox = e.getEntityBoundingBox().expand(0.1D + expand, 0.1D + expand, 0.1D + expand);
                        AxisAlignedBB axis = new AxisAlignedBB(bbox.minX - e.posX + x, bbox.minY - e.posY + y, bbox.minZ - e.posZ + z, bbox.maxX - e.posX + x, bbox.maxY - e.posY + y, bbox.maxZ - e.posZ + z);
                        glBlendFunc(770, 771);
                        glEnable(3042);
                        glDisable(3553);
                        glDisable(2929);
                        glDepthMask(false);
                        glLineWidth(2.0F);
                        glColor4f(r, g, b, a);
                        // box, shaded box
                        if (type == 1) {
                            RenderGlobal.drawSelectionBoundingBox(axis);
                        } else if (type == 2) {
                            drawBoundingBox(axis, r, g, b);
                        }

                        glEnable(3553);
                        glEnable(2929);
                        glDepthMask(true);
                        glDisable(3042);
                    }
                }
            }

            GlStateManager.popMatrix();
        }
    }

    public static void drawBoundingBox(AxisAlignedBB abb, float r, float g, float b) {
        float a = 0.25F;
        Tessellator ts = Tessellator.getInstance();
        WorldRenderer vb = ts.getWorldRenderer();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vb.pos(abb.minX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.minX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.minZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.maxY, abb.maxZ).color(r, g, b, a).endVertex();
        vb.pos(abb.maxX, abb.minY, abb.maxZ).color(r, g, b, a).endVertex();
        ts.draw();
    }

    public static void drawTracerLine(Entity e, int color, float lw) {
        if (e != null && MouseUtils.getTimer() != null) {
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double) MouseUtils.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosX;
            double y = (double) e.getEyeHeight() + e.lastTickPosY + (e.posY - e.lastTickPosY) * (double) MouseUtils.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosY;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double) MouseUtils.getTimer().renderPartialTicks - mc.getRenderManager().viewerPosZ;
            float a = (float) (color >> 24 & 255) / 255.0F;
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            glPushMatrix();
            glEnable(3042);
            glEnable(2848);
            glDisable(2929);
            glDisable(3553);
            glBlendFunc(770, 771);
            glEnable(3042);
            glLineWidth(lw);
            glColor4f(r, g, b, a);
            glBegin(2);
            glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
            glVertex3d(x, y, z);
            glEnd();
            glDisable(3042);
            glEnable(3553);
            glEnable(2929);
            glDisable(2848);
            glDisable(3042);
            glPopMatrix();
        }
    }

    public static void drawGradient(int left, int top, int right, int bottom, int startColor, int endColor) {
        int j;
        if (left < right) {
            j = left;
            left = right;
            right = j;
        }

        if (top < bottom) {
            j = top;
            top = bottom;
            bottom = j;
        }

        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void d2p(double x, double y, int radius, int sides, int color) {
        float a = (float) (color >> 24 & 255) / 255.0F;
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);
        worldrenderer.begin(6, DefaultVertexFormats.POSITION);

        for (int i = 0; i < sides; ++i) {
            double angle = 6.283185307179586D * (double) i / (double) sides + Math.toRadians(180.0D);
            worldrenderer.pos(x + Math.sin(angle) * (double) radius, y + Math.cos(angle) * (double) radius, 0.0D).endVertex();
        }

        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void startScale(float x, float y, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-x, -y, 0);
    }

    public static void startScale(float x, float y, float width, float height, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((x + (x + width)) / 2, (y + (y + height)) / 2, 0);
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(-(x + (x + width)) / 2, -(y + (y + height)) / 2, 0);
    }
}
