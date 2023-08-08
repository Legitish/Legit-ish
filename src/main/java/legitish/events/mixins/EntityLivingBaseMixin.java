package legitish.events.mixins;

import legitish.events.EventBus;
import legitish.events.impl.LivingUpdateEvent;
import legitish.main.Legitish;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {
    final EventBus eventBus = Legitish.getEventBus();

    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void injectLivingUpdateEventPre(final CallbackInfo callbackInfo) {
        eventBus.call(new LivingUpdateEvent(LivingUpdateEvent.Type.PRE));
    }

    @Inject(method = "onUpdate", at = @At("RETURN"))
    public void injectLivingUpdateEventPost(final CallbackInfo callbackInfo) {
        eventBus.call(new LivingUpdateEvent(LivingUpdateEvent.Type.POST));
    }
}
