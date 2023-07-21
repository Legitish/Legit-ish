package legitish.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.EXTPackedDepthStencil;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

public class StencilUtils {
    public static void setupFBO(Framebuffer framebuffer) {
        Minecraft mc = Minecraft.getMinecraft();
        glDeleteRenderbuffersEXT(framebuffer.depthBuffer);
        final int stencilDepthBufferID = glGenRenderbuffersEXT();
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, stencilDepthBufferID);
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, mc.displayWidth, mc.displayHeight);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_STENCIL_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, stencilDepthBufferID);
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT, GL_DEPTH_ATTACHMENT_EXT, GL_RENDERBUFFER_EXT, stencilDepthBufferID);
    }

    public static void enableStencilBuffer() {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getFramebuffer().bindFramebuffer(false);
        if (mc.getFramebuffer() != null) {
            if (mc.getFramebuffer().depthBuffer > -1) {
                setupFBO(mc.getFramebuffer());
                mc.getFramebuffer().depthBuffer = -1;
            }
        }
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);

        glStencilFunc(GL_ALWAYS, 1, 1);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glColorMask(false, false, false, false);
    }

    public static void readStencilBuffer(int ref) {
        glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, ref, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
    }

    public static void disableStencilBuffer() {
        glDisable(GL_STENCIL_TEST);
    }
}
