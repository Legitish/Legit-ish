package legitish.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GameUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean getWeapon() {
        if (mc.thePlayer.getCurrentEquippedItem() == null) {
            return false;
        } else {
            Item item = mc.thePlayer.getCurrentEquippedItem().getItem();
            return item instanceof ItemSword || item instanceof ItemAxe;
        }
    }

    public static boolean isPlayerInGame() {
        return mc.thePlayer != null && mc.theWorld != null;
    }

    public static void sendChat(String txt) {
        if (isPlayerInGame()) {
            String message = formatColorCode("[!] " + txt);
            mc.thePlayer.addChatMessage(new ChatComponentText(message));
        }
    }

    public static String formatColorCode(String txt) {
        return txt.replaceAll("&", "§");
    }

    public static List<String> getScoreboard() {
        List<String> lines = new ArrayList<>();
        if (mc.theWorld != null) {
            Scoreboard scoreboard = mc.theWorld.getScoreboard();
            if (scoreboard != null) {
                ScoreObjective objective = scoreboard.getObjectiveInDisplaySlot(1);
                if (objective != null) {
                    Collection<Score> scores = scoreboard.getSortedScores(objective);
                    List<Score> list = new ArrayList<>();
                    Iterator<Score> var5 = scores.iterator();

                    Score score;
                    while (var5.hasNext()) {
                        score = var5.next();
                        if (score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#")) {
                            list.add(score);
                        }
                    }

                    if (list.size() > 15) {
                        scores = Lists.newArrayList(Iterables.skip(list, scores.size() - 15));
                    } else {
                        scores = list;
                    }

                    var5 = scores.iterator();

                    while (var5.hasNext()) {
                        score = var5.next();
                        ScorePlayerTeam team = scoreboard.getPlayersTeam(score.getPlayerName());
                        lines.add(ScorePlayerTeam.formatPlayerName(team, score.getPlayerName()));
                    }

                }
            }
        }
        return lines;
    }

    public static double getBPS(Entity en, int d) {
        double x = en.posX - en.prevPosX;
        double z = en.posZ - en.prevPosZ;
        double sp = Math.sqrt(x * x + z * z) * 20.0D;
        return MathUtils.round(sp, d);
    }

    public static boolean fov(Entity entity, float fov) {
        fov = (float) ((double) fov * 0.5D);
        double v = ((double) (mc.thePlayer.rotationYaw - m(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
        return v > 0.0D && v < (double) fov || (double) (-fov) < v && v < 0.0D;
    }

    public static float[] gr(Entity q) {
        if (q == null) {
            return null;
        } else {
            double diffX = q.posX - mc.thePlayer.posX;
            double diffY;
            if (q instanceof EntityLivingBase) {
                EntityLivingBase en = (EntityLivingBase) q;
                diffY = en.posY + (double) en.getEyeHeight() * 0.9D - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
            } else {
                diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0D - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
            }

            double diffZ = q.posZ - mc.thePlayer.posZ;
            double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
            float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
            return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
        }
    }

    public static double n(Entity en) {
        return ((double) (mc.thePlayer.rotationYaw - m(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
    }

    public static float m(Entity ent) {
        double x = ent.posX - mc.thePlayer.posX;
        double z = ent.posZ - mc.thePlayer.posZ;
        double yaw = Math.atan2(x, z) * 57.2957795D;
        return (float) (yaw * -1.0D);
    }

    public static void aim(Entity en, float ps, boolean silent) {
        if (en != null) {
            float[] t = gr(en);
            if (t != null) {
                float y = t[0];
                float p = t[1] + 4.0F + ps;
                if (silent) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(y, p, mc.thePlayer.onGround));
                } else {
                    mc.thePlayer.rotationYaw = y;
                    mc.thePlayer.rotationPitch = p;
                }
            }
        }
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
    }

    public static boolean playerAboveAir() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY - 1.0D;
        double z = mc.thePlayer.posZ;
        BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
        return mc.theWorld.isAirBlock(p);
    }
}
