package legitish.utils.render;

import legitish.utils.ColorUtils;
import legitish.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glUniform1;

public class GaussianBlur {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtils blurShader = new ShaderUtils("legitish/shaders/gaussian.frag");
    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);

    public static void setupUniforms(double dir1, double dir2, double radius) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0F / (double) mc.displayWidth, 1.0F / (double) mc.displayHeight);
        blurShader.setUniformf("direction", dir1, dir2);
        blurShader.setUniformf("radius", radius);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(MathUtils.calculateGaussianValue(i, radius / 2));
        }
        weightBuffer.rewind();
        glUniform1(blurShader.getUniform("weights"), weightBuffer);
    }

    public static void renderBlur(double radius) {
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        framebuffer = RenderUtils.createFrameBuffer(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        blurShader.init();
        setupUniforms(1, 0, radius);
        RenderUtils.bindTexture(mc.getFramebuffer().framebufferTexture);
        ShaderUtils.drawQuads();
        framebuffer.unbindFramebuffer();
        blurShader.unload();
        mc.getFramebuffer().bindFramebuffer(true);
        blurShader.init();
        setupUniforms(0, 1, radius);
        RenderUtils.bindTexture(framebuffer.framebufferTexture);
        ShaderUtils.drawQuads();
        blurShader.unload();
        ColorUtils.resetColor();
        GlStateManager.bindTexture(0);
    }
}