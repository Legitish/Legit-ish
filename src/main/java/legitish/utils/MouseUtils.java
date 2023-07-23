package legitish.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MouseUtils {
    public static final Minecraft mc = Minecraft.getMinecraft();
    private static final List<Long> a = new ArrayList<>();
    private static final List<Long> b = new ArrayList<>();
    public static long LL = 0L;
    public static long LR = 0L;
    private static Field timer = null;
    private static Field g = null;
    private static Field f = null;
    private static Field h = null;

    public static void setFields() {
        try {
            timer = Minecraft.class.getDeclaredField("timer");
        } catch (Exception ignored) {

        }

        if (timer != null) {
            timer.setAccessible(true);
        }

        try {
            g = MouseEvent.class.getDeclaredField("button");
            f = MouseEvent.class.getDeclaredField("buttonState");
            h = Mouse.class.getDeclaredField("buttons");
        } catch (Exception ignored) {
        }
    }

    // Remind me to replace this with Java's bot because I'm pretty sure it works better.
    public static void sendClick(int button, boolean press) {
        if (g != null && f != null && h != null) {
            MouseEvent m = new MouseEvent();

            try {
                g.setAccessible(true);
                g.set(m, button);
                f.setAccessible(true);
                f.set(m, press);
                h.setAccessible(true);
                ByteBuffer bf = (ByteBuffer) h.get(null);
                h.setAccessible(false);
                bf.put(button, (byte) (press ? 1 : 0));
            } catch (IllegalAccessException ignored) {
            }
        }
    }

    public static net.minecraft.util.Timer getTimer() {
        try {
            return (net.minecraft.util.Timer) timer.get(mc);
        } catch (IndexOutOfBoundsException | IllegalAccessException var1) {
            return null;
        }
    }

    public static boolean mouseInBounds(int mouseX, int mouseY, double x, double y, double width, double height) {
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
