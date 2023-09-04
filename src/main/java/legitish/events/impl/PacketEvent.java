package legitish.events.impl;

import legitish.events.Event;
import net.minecraft.network.Packet;

public class PacketEvent extends Event {
    public final Packet<?> packet;
    public final Direction type;

    public PacketEvent(final Packet<?> packet, final Direction direction) {
        this.packet = packet;
        this.type = direction;
    }

    public <T extends Packet<?>> T getPacket() {
        return (T) this.packet;
    }

    public enum Direction {
        INCOMING,
        OUTGOING
    }
}
