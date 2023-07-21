package legitish.main;

import com.google.common.eventbus.EventBus;
import legitish.config.ClientConfig;
import legitish.config.ConfigManager;
import legitish.gui.ClickGui;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Legitish {
    public static final EventBus eventBus = new EventBus();
    private static final ScheduledExecutorService ex = Executors.newScheduledThreadPool(2);
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ConfigManager configManager;
    public static ClientConfig clientConfig;
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;

    public Legitish() {
        moduleManager = new ModuleManager();
    }

    public static void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(ex::shutdown));
        net.weavemc.loader.api.event.EventBus.subscribe(new Legitish());
        net.weavemc.loader.api.event.EventBus.subscribe(new MouseUtils());
        FontUtils.init();
        clickGui = new ClickGui();
        moduleManager.RegisterMods();
        MouseUtils.setFields();
        configManager = new ConfigManager();
        clientConfig = new ClientConfig();
        clientConfig.applyConfig();
    }

    public static ScheduledExecutorService getExecutor() {
        return ex;
    }

    @SubscribeEvent
    public void clientTimer(RenderWorldEvent e) {
        if (GameUtils.isPlayerInGame()) {
            for (Module module : moduleManager.moduleList()) {
                if (mc.currentScreen == null) {
                    module.keybind();
                } else if (mc.currentScreen instanceof ClickGui) {
                    module.guiUpdate();
                }

                if (module.isEnabled()) {
                    module.update();
                }
            }
        }
    }
}


