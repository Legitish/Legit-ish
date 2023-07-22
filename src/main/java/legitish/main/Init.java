package legitish.main;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.StartGameEvent;

public class Init implements ModInitializer {
    //i wanted to put this part in the main class but it wouldnt work why? idk
    @Override
    public void preInit() {
        EventBus.subscribe(StartGameEvent.Post.class, startGameEvent -> Legitish.init());
    }
}
