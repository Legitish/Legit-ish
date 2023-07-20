package legitish.utils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import legitish.main.Legitish;
import legitish.module.modulesettings.ModuleDoubleSliderSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.*;

public class MouseUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private static final Random rand = new Random();
    private static final List<Long> a = new ArrayList<>();
    private static final List<Long> b = new ArrayList<>();
    public static long LL = 0L;
    public static long LR = 0L;
    private static Field t = null;
    private static Field g = null;
    private static Field f = null;
    private static Field h = null;

    public static void setFields() {
        try {
            t = Minecraft.class.getDeclaredField("field_71428_T");
        } catch (Exception var4) {
            try {
                t = Minecraft.class.getDeclaredField("timer");
            } catch (Exception ignored) {
            }
        }

        if (t != null) {
            t.setAccessible(true);
        }

        try {
            g = MouseEvent.class.getDeclaredField("button");
            f = MouseEvent.class.getDeclaredField("buttonstate");
            h = Mouse.class.getDeclaredField("buttons");
        } catch (Exception ignored) {
        }
    }

    public static void sc(int t, boolean s) {
        if (g != null && f != null && h != null) {
            MouseEvent m = new MouseEvent();

            try {
                g.setAccessible(true);
                g.set(m, t);
                f.setAccessible(true);
                f.set(m, s);
                Legitish.eventBus.post(m);
                h.setAccessible(true);
                ByteBuffer bf = (ByteBuffer) h.get(null);
                h.setAccessible(false);
                bf.put(t, (byte) (s ? 1 : 0));
            } catch (IllegalAccessException ignored) {
            }

        }
    }

    public static void b(ModuleDoubleSliderSetting a) {
        if (a.getInputMin() > a.getInputMax()) {
            double p = a.getInputMin();
            a.setValueMin(a.getInputMax());
            a.setValueMax(p);
        }
    }

    public static double mmVal(ModuleDoubleSliderSetting a, Random r) {
        return a.getInputMin() == a.getInputMax() ? a.getInputMin() : a.getInputMin() + r.nextDouble() * (a.getInputMax() - a.getInputMin());
    }

    public static int f() {
        return mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime();
    }

    public static net.minecraft.util.Timer getTimer() {
        try {
            return (net.minecraft.util.Timer) t.get(mc);
        } catch (IndexOutOfBoundsException | IllegalAccessException var1) {
            return null;
        }
    }

    public static int randomInt(double inputMin, double v) {
        return (int) ((Math.random() * (v - inputMin)) + inputMin);
    }

    public static Random rand() {
        return rand;
    }

    public static boolean isMoving() {
        return mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F;
    }

    public static void aim(Entity en, float ps, boolean silent) {
        if (en != null) {
            float[] t = gr(en);
            if (t != null) {
                float y = t[0];
                float p = t[1] + 4.0F + ps;
                if (silent) {
                    mc.getNetHandler().addToSendQueue(new C05PacketPlayerLook(y, p, mc.thePlayer.onGround));
                } else {
                    mc.thePlayer.rotationYaw = y;
                    mc.thePlayer.rotationPitch = p;
                }
            }
        }
    }

    public static boolean isInside(int mouseX, int mouseY, double x, double y, double width, double height) {
        return (mouseX > (int) x && mouseX < (int) (x + width)) && (mouseY > (int) y && mouseY < (int) (y + height));
    }

    public static Scroll scroll() {
        int mouse = Mouse.getDWheel();

        if (mouse > 0) {
            return Scroll.UP;
        } else if (mouse < 0) {
            return Scroll.DOWN;
        } else {
            return null;
        }
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

    public static boolean fov(Entity entity, float fov) {
        fov = (float) ((double) fov * 0.5D);
        double v = ((double) (mc.thePlayer.rotationYaw - m(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
        return v > 0.0D && v < (double) fov || (double) (-fov) < v && v < 0.0D;
    }

    public static PositionMode getPostitionMode(int marginX, int marginY, double height, double width) {
        int halfHeight = (int) (height / 4);
        int halfWidth = (int) width;
        PositionMode positionMode = null;

        if (marginY < halfHeight) {
            if (marginX < halfWidth) {
                positionMode = PositionMode.UPLEFT;
            }
            if (marginX > halfWidth) {
                positionMode = PositionMode.UPRIGHT;
            }
        }

        if (marginY > halfHeight) {
            if (marginX < halfWidth) {
                positionMode = PositionMode.DOWNLEFT;
            }
            if (marginX > halfWidth) {
                positionMode = PositionMode.DOWNRIGHT;
            }
        }

        return positionMode;
    }

    public static double getBPS(Entity en, int d) {
        double x = en.posX - en.prevPosX;
        double z = en.posZ - en.prevPosZ;
        double sp = Math.sqrt(x * x + z * z) * 20.0D;
        return MathUtils.round(sp, d);
    }

    public static int gc(long speed, long... delay) {
        long time = System.currentTimeMillis() + (delay.length > 0 ? delay[0] : 0L);
        return Color.getHSBColor((float) (time % (15000L / speed)) / (15000.0F / (float) speed), 1.0F, 1.0F).getRGB();
    }

    public static String str(String s) {
        char[] n = StringUtils.stripControlCodes(s).toCharArray();
        StringBuilder v = new StringBuilder();

        for (char c : n) {
            if (c < 127 && c > 20) {
                v.append(c);
            }
        }

        return v.toString();
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

    public static boolean playerAboveAir() {
        double x = mc.thePlayer.posX;
        double y = mc.thePlayer.posY - 1.0D;
        double z = mc.thePlayer.posZ;
        BlockPos p = new BlockPos(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
        return mc.theWorld.isAirBlock(p);
    }

    public static void aL() {
        a.add(LL = System.currentTimeMillis());
    }

    public static void aR() {
        b.add(LR = System.currentTimeMillis());
    }

    public static int v() {
        a.removeIf(o -> o < System.currentTimeMillis() - 1000L);
        return a.size();
    }

    public static int i() {
        b.removeIf(o -> o < System.currentTimeMillis() - 1000L);
        return b.size();
    }

    @SubscribeEvent
    public void onMouseUpdate(MouseEvent d) {
        if (d.getButtonState()) {
            if (d.getButton() == 0) {
                aL();
            } else if (d.getButton() == 1) {
                aR();
            }
        }
    }

    public enum Scroll {
        UP, DOWN
    }

    public enum PositionMode {
        UPLEFT,
        UPRIGHT,
        DOWNLEFT,
        DOWNRIGHT
    }
}
