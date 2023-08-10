package legitish.module.modules.client;

import legitish.events.Subscribe;
import legitish.events.impl.RenderGameOverlayEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.AnimationUtils;
import legitish.utils.ColorUtils;
import legitish.utils.CooldownUtils;
import legitish.utils.GameUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Notifications extends Module {
    public static final List<NotificationTypes> notifs = new ArrayList<>();
    private static final List<String> messages = new ArrayList<>();
    private static final List<CooldownUtils> durations = new ArrayList<>();
    private static final List<AnimationUtils> animationsX = new ArrayList<>();
    private static final List<AnimationUtils> animationsY = new ArrayList<>();
    public static ModuleTickSetting chatNoti;

    public Notifications() {
        super("Notifications", category.Client, 0);
        this.registerSetting(new ModuleDesc("Displays notifications for certain events."));
        this.registerSetting(chatNoti = new ModuleTickSetting("Show in chat", false));
    }

    public static void sendNotification(NotificationTypes notificationType, String message, long duration) {
        if (!chatNoti.isToggled()) {
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            notifs.add(notificationType);
            messages.add(message);
            durations.add(new CooldownUtils(duration));
            durations.get(notifs.size() - 1).start();
            animationsX.add(new AnimationUtils(sr.getScaledWidth()));
            animationsY.add(new AnimationUtils(sr.getScaledHeight() - (notifs.size() * 30)));
            animationsX.get(notifs.size() - 1).setAnimation(sr.getScaledWidth(), 16);
        } else {
            GameUtils.sendChat("&7[&1LI&7-" + ((notificationType == NotificationTypes.INFO) ? "&1" : notificationType == NotificationTypes.WARN ? "&e" : "&4") + notificationType.toString() + "&7]&r " + message);
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = RenderGameOverlayEvent.class)
    public void onTick(RenderGameOverlayEvent event) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        for (int index = 0; index < notifs.size(); index++) {
            animationsY.get(index).setAnimation(sr.getScaledHeight() - ((index + 1) * 30), 16);
            RRectUtils.drawRound(animationsX.get(index).getValue(), animationsY.get(index).getValue(), 120, 25, 3, new Color(0, 0, 0, 150));
            FontUtils.icon20.drawString(notifs.get(index) == NotificationTypes.INFO ? "G" : "R", animationsX.get(index).getValue() + 12.5, animationsY.get(index).getValue() + 12.5, MinecraftFontRenderer.CenterMode.XY, false, ColorUtils.getFontColor(2).getRGB());
            FontUtils.regular16.wrapText(messages.get(index), animationsX.get(index).getValue() + 25, animationsY.get(index).getValue() + 12.5, MinecraftFontRenderer.CenterMode.Y, false, ColorUtils.getFontColor(2).getRGB(), 95);
            if (durations.get(index).hasFinished()) {
                notifs.remove(index);
                messages.remove(index);
                durations.remove(index);
                animationsX.remove(index);
                animationsY.remove(index);
                index--;
            } else if (durations.get(index).getTimeLeft() < 500) {
                animationsX.get(index).setAnimation(sr.getScaledWidth(), 16);
            } else {
                animationsX.get(index).setAnimation(sr.getScaledWidth() - 125, 16);
            }
        }
    }

    public enum NotificationTypes {
        INFO,
        WARN,
        ERROR
    }
}
