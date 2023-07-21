package legitish.gui;

import legitish.utils.ColorUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Notification {
    public void sendNotification(NotificationTypes notificationType, String message, double duration) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        RRectUtils.drawRound(sr.getScaledWidth() - 50, sr.getScaledHeight() - 30, 40, 20, 3, ColorUtils.getBackgroundColor(4));
        FontUtils.regular16.wrapText(message, sr.getScaledWidth() - 50 + 2.5, sr.getScaledHeight() - 30, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB(), 45, 7);
    }

    public enum NotificationTypes {
        INFO,
        WARN,
        ERROR
    }
}
