package legitish.module.modules.player;

import legitish.events.Subscribe;
import legitish.events.ext.EventDirection;
import legitish.events.impl.PacketEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public class Blink extends Module {
    public static ModuleTickSetting inbound, outbound, spawnFake;

    private final ArrayList<? extends Packet> outboundPackets = new ArrayList<>();
    private final ArrayList<? extends Packet> inboundPackets = new ArrayList<>();
    private static EntityOtherPlayerMP fakePlayer;

    public Blink() {
        super("Blink", category.Player, 0);
        this.registerSetting(new ModuleDesc("Chokes packets until disabled."));
        this.registerSetting(inbound = new ModuleTickSetting("Block Inbound", true));
        this.registerSetting(outbound = new ModuleTickSetting("Block Outbound", true));
        this.registerSetting(spawnFake = new ModuleTickSetting("Spawn fake player", true));
    }

    @Subscribe(eventClass = PacketEvent.class)
    public void onPacket(PacketEvent e) {
        if (e.getDirection() == EventDirection.INCOMING) {
            if (!inbound.isToggled()) return;
            inboundPackets.add(e.getPacket());
        } else {
            if (!outbound.isToggled()) return;
            if (!e.getPacket().getClass().getCanonicalName().startsWith("net.minecraft.network.play.client")) return;
            outboundPackets.add(e.getPacket());
        }
        e.setCancelled(true);
    }

    @Override
    public void onEnable() {
        outboundPackets.clear();
        inboundPackets.clear();
        if (spawnFake.isToggled()) {
            if (mc.thePlayer != null) {
                fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
                fakePlayer.setRotationYawHead(mc.thePlayer.rotationYawHead);
                fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
                mc.theWorld.addEntityToWorld(fakePlayer.getEntityId(), fakePlayer);
            }
        }
    }

    @Override
    public void onDisable() {
        for (Packet packet : outboundPackets) {
            mc.getNetHandler().addToSendQueue(packet);
        }

        outboundPackets.clear();
        inboundPackets.clear();
        if (fakePlayer != null) {
            mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
            fakePlayer = null;
        }
    }
}
