package legitish.module.modules.render;

import legitish.module.Module;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import static org.lwjgl.opengl.GL11.*;

public class Chams extends Module {
    public Chams() {
        super("Chams", category.Visual, 0);
    }

    @SubscribeEvent
    public void render1(RenderLivingEvent.Pre e) {
        if (e.getEntity() != mc.thePlayer) {
            glEnable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, -1100000.0F);
        }
    }

    @SubscribeEvent
    public void render2(RenderLivingEvent.Post e) {
        if (e.getEntity() != mc.thePlayer) {
            glDisable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, 1100000.0F);
        }
    }
}
