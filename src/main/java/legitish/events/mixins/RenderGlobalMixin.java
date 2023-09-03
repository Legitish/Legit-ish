package legitish.events.mixins;

import legitish.events.EventBus;
import legitish.events.impl.DrawBlockHighlightEvent;
import legitish.main.Legitish;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin {
    final EventBus eventBus = Legitish.getEventBus();
    final Minecraft mc = Minecraft.getMinecraft();

    @Inject(method = "drawSelectionBox", at = @At("RETURN"))
    public void injectDrawBlockHighlightEvent(EntityPlayer player, MovingObjectPosition movingObjectPositionIn, int execute, float partialTicks, final CallbackInfo ci) {
        if (execute == 0 && movingObjectPositionIn.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos blockpos = movingObjectPositionIn.getBlockPos();
            Block block = mc.theWorld.getBlockState(blockpos).getBlock();
            if (block.getMaterial() != Material.air && mc.theWorld.getWorldBorder().contains(blockpos)) {
                eventBus.call(new DrawBlockHighlightEvent(blockpos));
            }
        }
    }
}
