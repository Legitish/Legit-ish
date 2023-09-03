package legitish.events.mixins;

import legitish.events.EventBus;
import legitish.events.impl.ClientTickEvent;
import legitish.events.impl.MouseEvent;
import legitish.events.impl.PlayerTickEvent;
import legitish.main.Legitish;
import legitish.module.ModuleManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    final EventBus eventBus = Legitish.getEventBus();

    @Shadow
    public int leftClickCounter;

    @Inject(method = "runTick", at = @At("HEAD"))
    public void injectClientTickEvent(final CallbackInfo ci) {
        eventBus.call(new ClientTickEvent());
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    public void injectPlayerTickEvent(final CallbackInfo ci) {
        eventBus.call(new PlayerTickEvent());
    }

    @Inject(method = "clickMouse", at = @At("HEAD"), cancellable = true)
    public void injectMouseEventLeft(final CallbackInfo ci) {
        MouseEvent event = new MouseEvent(MouseEvent.Button.LEFT);
        eventBus.call(event);

        if (event.isCancelled) {
            ci.cancel();
        }
    }

    @Inject(method = "rightClickMouse", at = @At("HEAD"), cancellable = true)
    public void injectMouseEventRight(final CallbackInfo ci) {
        MouseEvent event = new MouseEvent(MouseEvent.Button.RIGHT);
        eventBus.call(event);

        if (event.isCancelled) {
            ci.cancel();
        }
    }

    @Inject(method = "clickMouse", at = @At("HEAD")) // Hardcoding delay remover, it just makes it easier
    private void clickMouseAfter(final CallbackInfo ci) {
        if (ModuleManager.delayremover.isEnabled()) {
            leftClickCounter = 0;
        }
    }
}
