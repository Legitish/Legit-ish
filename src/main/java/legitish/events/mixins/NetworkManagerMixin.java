package legitish.events.mixins;

import io.netty.channel.ChannelHandlerContext;
import legitish.events.EventBus;
import legitish.events.impl.PacketEvent;
import legitish.main.Legitish;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(priority = 995, value = NetworkManager.class)
public class NetworkManagerMixin {
    final EventBus eventBus = Legitish.getEventBus();

    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void sendPacket(Packet p_sendPacket_1_, CallbackInfo ci) {
        PacketEvent e = new PacketEvent(p_sendPacket_1_, PacketEvent.Direction.OUTGOING);
        eventBus.call(e);
        if (e.isCancelled) ci.cancel();
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void receivePacket(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_, CallbackInfo ci) {
        PacketEvent e = new PacketEvent(p_channelRead0_2_, PacketEvent.Direction.INCOMING);
        eventBus.call(e);
        if (e.isCancelled) ci.cancel();
    }

}
