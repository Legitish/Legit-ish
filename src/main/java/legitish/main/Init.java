package legitish.main;

import legitish.module.ModuleManager;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.*;

public class Init implements ModInitializer {
    @Override
    public void preInit() {
        EventBus.subscribe(this);
    }

    @SubscribeEvent
    public void onGameStart(StartGameEvent.Post e) {
        Legitish.init();
    }

    @SubscribeEvent
    public void onShutdown(ShutdownEvent e) {
        if (ModuleManager.blink.isEnabled()) {
            ModuleManager.blink.disable();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent e) {
        if (ModuleManager.blink.isEnabled()) {
            ModuleManager.blink.disable();
        }
    }

    // Note: blink is disabled on game start, shutdown, and world load to avoid it bugging out, please do not remove unless you plan to use a better implementation.
}
