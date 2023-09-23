package legitish.module.modules.render;

import legitish.events.Subscribe;
import legitish.events.impl.RenderLivingEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import static org.lwjgl.opengl.GL11.*;

public class Chams extends Module {
    public Chams() {
        super("Chams", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Render players through walls."));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderLivingEvent.class)
    public void renderChams(RenderLivingEvent event) {
        if (event.type == RenderLivingEvent.Type.PRE) {
            if (event.entity instanceof EntityPlayer && event.entity != Minecraft.getMinecraft().thePlayer) {
                glEnable(GL_POLYGON_OFFSET_FILL);
                glPolygonOffset(1.0F, -1100000.0F);
            }
        } else if (event.type == RenderLivingEvent.Type.POST) {
            if (event.entity != mc.thePlayer) {
                glDisable(GL_POLYGON_OFFSET_FILL);
                glPolygonOffset(1.0F, 1100000.0F);
            }
        }
    }
}
