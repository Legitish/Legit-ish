package legitish.main;

import legitish.config.ClientConfig;
import legitish.config.ConfigManager;
import legitish.gui.ClickGui;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;


public class Legitish {
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static ConfigManager configManager;
    public static ClientConfig clientConfig;
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;

    public Legitish() {
        moduleManager = new ModuleManager();
    }

    public static void init() {
        EventBus.subscribe(new Legitish());
        EventBus.subscribe(new MouseUtils());
        FontUtils.init();
        clickGui = new ClickGui();
        moduleManager.RegisterMods();
        MouseUtils.setFields();
        configManager = new ConfigManager();
        clientConfig = new ClientConfig();
        clientConfig.applyConfig();
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


