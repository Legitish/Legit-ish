package legitish.module.modules.player;

import legitish.events.Subscribe;
import legitish.events.impl.PacketEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public class Blink extends Module {
    public static ModuleTickSetting spawnFake;
    private static EntityOtherPlayerMP fakePlayer;
    private final ArrayList<? extends Packet> outboundPackets = new ArrayList<>();

    public Blink() {
        super("Blink", category.Player, 0);
        this.registerSetting(new ModuleDesc("Chokes packets until disabled."));
        this.registerSetting(spawnFake = new ModuleTickSetting("Spawn fake player", true));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = PacketEvent.class)
    public void onPacket(PacketEvent event) {
        if (event.type == PacketEvent.Direction.OUTGOING) {
            if (!event.getPacket().getClass().getCanonicalName().startsWith("net.minecraft.network.play.client"))
                return;
            outboundPackets.add(event.getPacket());
        }
        event.setCancelled(true);
    }

    @Override
    public void onEnable() {
        outboundPackets.clear();
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
        if (fakePlayer != null) {
            mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
            fakePlayer = null;
        }
    }
}
