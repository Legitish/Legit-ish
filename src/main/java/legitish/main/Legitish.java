package legitish.main;

import legitish.config.ClientConfig;
import legitish.config.ConfigManager;
import legitish.events.EventBus;
import legitish.events.Subscribe;
import legitish.events.impl.ClientTickEvent;
import legitish.gui.ClickGui;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import net.minecraft.client.Minecraft;


public class Legitish {
    private static final EventBus eventBus = new EventBus();
    public static ConfigManager configManager;
    public static ClientConfig clientConfig;
    public static ModuleManager moduleManager;
    public static ClickGui clickGui;
    private static Minecraft mc = null;

    public Legitish() {
        mc = Minecraft.getMinecraft();
        moduleManager = new ModuleManager();
    }

    public static void init() {
        getEventBus().subscribe(new Legitish());
        getEventBus().subscribe(new MouseUtils());
        FontUtils.init();
        clickGui = new ClickGui();
        moduleManager.RegisterMods();
        MouseUtils.setFields();
        configManager = new ConfigManager();
        clientConfig = new ClientConfig();
        clientConfig.applyConfig();
    }

    public static EventBus getEventBus() {
        return eventBus;
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = ClientTickEvent.class)
    public void clientTimer(ClientTickEvent event) {
        if (GameUtils.isPlayerInGame()) {
            for (Module module : moduleManager.moduleList()) {
                if (mc.currentScreen == null) {
                    module.keybind();
                } else if (mc.currentScreen instanceof ClickGui) {
                    module.guiUpdate();
                }
            }
        }
    }
}


