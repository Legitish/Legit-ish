package legitish.events.mixins;

import legitish.events.EventBus;
import legitish.events.impl.ClientTickEvent;
import legitish.events.impl.MouseEvent;
import legitish.main.Legitish;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    final EventBus eventBus = Legitish.getEventBus();

    @Inject(method = "runTick", at = @At("RETURN"))
    public void injectTickEventPost(final CallbackInfo callbackInfo) {
        eventBus.call(new ClientTickEvent());
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    public void injectMouseEventLeft(final CallbackInfo callbackInfo) {
        MouseEvent event = new MouseEvent(MouseEvent.Button.LEFT);
        eventBus.call(event);

        if (event.isCancelled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    public void injectMouseEventRight(final CallbackInfo callbackInfo) {
        MouseEvent event = new MouseEvent(MouseEvent.Button.RIGHT);
        eventBus.call(event);

        if (event.isCancelled) {
            callbackInfo.cancel();
        }
    }
}
