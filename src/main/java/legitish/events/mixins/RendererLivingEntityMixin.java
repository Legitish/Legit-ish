package legitish.events.mixins;

import legitish.events.EventBus;
import legitish.events.impl.RenderLivingEvent;
import legitish.events.impl.RenderNameplateEvent;
import legitish.main.Legitish;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public class RendererLivingEntityMixin {
    final EventBus eventBus = Legitish.getEventBus();

    @Inject(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At("HEAD"), cancellable = true)
    public void injectRenderNameplateEvent(EntityLivingBase t, double v, double v1, double v2, final CallbackInfo callbackInfo) {
        RenderNameplateEvent event = new RenderNameplateEvent(t, v, v1, v2);
        eventBus.call(event);

        if (event.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("HEAD"))
    public void injectRenderLivingEventPre(EntityLivingBase t, double v, double v1, double v2, float v3, float v4, final CallbackInfo callbackInfo) {
        eventBus.call(new RenderLivingEvent(t, v, v1, v2, v3, v4, RenderLivingEvent.Type.PRE));
    }

    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At("RETURN"))
    public void injectRenderLivingEventPost(EntityLivingBase t, double v, double v1, double v2, float v3, float v4, final CallbackInfo callbackInfo) {
        eventBus.call(new RenderLivingEvent(t, v, v1, v2, v3, v4, RenderLivingEvent.Type.POST));
    }
}
