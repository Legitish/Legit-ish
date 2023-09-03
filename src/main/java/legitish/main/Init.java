package legitish.main;

import legitish.module.ModuleManager;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.*;

public class Init implements ModInitializer {
    // If I put ANYTHING else in the preInit() function the mod doesnt initialize for some reason ??????
    @Override
    public void preInit() {
        EventBus.subscribe(this);
    }

    @SubscribeEvent
    public void onGameStart(StartGameEvent.Post e) {
        Legitish.init();
        ModuleManager.blink.disable();
    }

    @SubscribeEvent
    public void onShutdown(ShutdownEvent e) {
        ModuleManager.blink.disable();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent e) {
        ModuleManager.blink.disable();
    }

    // Note: blink is disabled on game start, shutdown, and world load to avoid it bugging out, please do not remove unless you plan to use a better implementation.
}
