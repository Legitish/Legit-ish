package legitish.events.mixins;

import legitish.events.EventBus;
import legitish.events.impl.RenderWorldEvent;
import legitish.main.Legitish;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    final EventBus eventBus = Legitish.getEventBus();

    @Inject(method = "renderWorldPass", at = @At("RETURN"))
    public void injectRenderWorldEvent(final CallbackInfo callbackInfo) {
        eventBus.call(new RenderWorldEvent());
    }
}
