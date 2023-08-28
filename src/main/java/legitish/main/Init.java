package legitish.main;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.StartGameEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

public class Init implements ModInitializer {
    // If I put ANYTHING else in the preInit() function the mod doesnt initialize for some reason ??????
    @Override
    public void preInit() {
        EventBus.subscribe(this);
    }

    @SubscribeEvent
    public void onGameStart(StartGameEvent.Post e) {
        Legitish.init();
    }
}
