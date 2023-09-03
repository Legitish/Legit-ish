package legitish.events.mixins;

import legitish.events.EventBus;
import legitish.events.impl.RenderGameOverlayEvent;
import legitish.main.Legitish;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin {
    final EventBus eventBus = Legitish.getEventBus();

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    public void injectRenderGameOverlayEvent(final CallbackInfo ci) {
        eventBus.call(new RenderGameOverlayEvent());
    }
}
