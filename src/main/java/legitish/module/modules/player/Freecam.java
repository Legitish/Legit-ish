package legitish.module.modules.player;

import legitish.events.Subscribe;
import legitish.events.impl.MouseEvent;
import legitish.events.impl.PlayerTickEvent;
import legitish.events.impl.RenderWorldEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class Freecam extends Module {
    public static ModuleSliderSetting speed;
    public static ModuleTickSetting disableOnDamage;
    public static EntityOtherPlayerMP en = null;
    private final float[] sAng = new float[]{0.0F, 0.0F};
    private int[] lcc = new int[]{Integer.MAX_VALUE, 0};

    public Freecam() {
        super("Freecam", category.Player, 0);
        this.registerSetting(new ModuleDesc("Allows you to move your camera outside of the player."));
        this.registerSetting(speed = new ModuleSliderSetting("Speed", 2.5D, 0.5D, 10.0D, 0.5D));
        this.registerSetting(disableOnDamage = new ModuleTickSetting("Disable on damage", true));
    }

    public void onEnable() {
        if (!mc.thePlayer.onGround) {
            this.disable();
        } else {
            en = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
            en.copyLocationAndAnglesFrom(mc.thePlayer);
            this.sAng[0] = en.rotationYawHead = mc.thePlayer.rotationYawHead;
            this.sAng[1] = mc.thePlayer.rotationPitch;
            en.setVelocity(0.0D, 0.0D, 0.0D);
            en.setInvisible(true);
            mc.theWorld.addEntityToWorld(-8008, en);
            mc.setRenderViewEntity(en);
        }
    }

    public void onDisable() {
        if (en != null) {
            mc.setRenderViewEntity(mc.thePlayer);
            mc.thePlayer.rotationYaw = mc.thePlayer.rotationYawHead = this.sAng[0];
            mc.thePlayer.rotationPitch = this.sAng[1];
            mc.theWorld.removeEntity(en);
            en = null;
        }

        this.lcc = new int[]{Integer.MAX_VALUE, 0};
        int x = mc.thePlayer.chunkCoordX;
        int z = mc.thePlayer.chunkCoordZ;

        for (int x2 = -1; x2 <= 1; ++x2) {
            for (int z2 = -1; z2 <= 1; ++z2) {
                int a = x + x2;
                int b = z + z2;
                mc.theWorld.markBlockRangeForRenderUpdate(a * 16, 0, b * 16, a * 16 + 15, 256, b * 16 + 15);
            }
        }

    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = PlayerTickEvent.class)
    public void onTick(PlayerTickEvent event) {
        if (disableOnDamage.isToggled() && mc.thePlayer.hurtTime != 0) {
            this.disable();
        } else {
            mc.thePlayer.setSprinting(false);
            mc.thePlayer.moveForward = 0.0F;
            mc.thePlayer.moveStrafing = 0.0F;
            en.rotationYaw = en.rotationYawHead = mc.thePlayer.rotationYaw;
            en.rotationPitch = mc.thePlayer.rotationPitch;
            double speed = 0.215D * Freecam.speed.getInput();
            EntityOtherPlayerMP freecam;
            double rad;
            double dx;
            double dz;
            double toRad = 0.017453292519943295D;
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                rad = (double) en.rotationYawHead * toRad;
                dx = -1.0D * Math.sin(rad) * speed;
                dz = Math.cos(rad) * speed;
                freecam = en;
                freecam.posX += dx;
                freecam.posZ += dz;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                rad = (double) en.rotationYawHead * toRad;
                dx = -1.0D * Math.sin(rad) * speed;
                dz = Math.cos(rad) * speed;
                freecam = en;
                freecam.posX -= dx;
                freecam.posZ -= dz;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode())) {
                rad = (double) (en.rotationYawHead - 90.0F) * toRad;
                dx = -1.0D * Math.sin(rad) * speed;
                dz = Math.cos(rad) * speed;
                freecam = en;
                freecam.posX += dx;
                freecam.posZ += dz;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode())) {
                rad = (double) (en.rotationYawHead + 90.0F) * toRad;
                dx = -1.0D * Math.sin(rad) * speed;
                dz = Math.cos(rad) * speed;
                freecam = en;
                freecam.posX += dx;
                freecam.posZ += dz;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                freecam = en;
                freecam.posY += 0.93D * speed;
            }

            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                freecam = en;
                freecam.posY -= 0.93D * speed;
            }

            mc.thePlayer.setSneaking(false);
            if (this.lcc[0] != Integer.MAX_VALUE && (this.lcc[0] != en.chunkCoordX || this.lcc[1] != en.chunkCoordZ)) {
                int x = en.chunkCoordX;
                int z = en.chunkCoordZ;
                mc.theWorld.markBlockRangeForRenderUpdate(x * 16, 0, z * 16, x * 16 + 15, 256, z * 16 + 15);
            }

            this.lcc[0] = en.chunkCoordX;
            this.lcc[1] = en.chunkCoordZ;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderWorldEvent.class)
    public void renderPlayer(RenderWorldEvent event) {
        if (GameUtils.isPlayerInGame()) {
            mc.thePlayer.renderArmPitch = mc.thePlayer.prevRenderArmPitch = 700.0F;
            GLUtils.RenderESP(mc.thePlayer, GLUtils.ESPTypes.SHADED, 0.0D, 0.0D, Color.green.getRGB(), false);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = MouseEvent.class)
    public void cancelMouseClicks(MouseEvent event) {
        if (GameUtils.isPlayerInGame()) {
            event.setCancelled(true);
        }
    }
}
