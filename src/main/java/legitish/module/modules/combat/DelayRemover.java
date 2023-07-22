package legitish.module.modules.combat;

import legitish.module.Module;
import legitish.module.ModuleDesc;
import legitish.utils.GameUtils;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

import java.lang.reflect.Field;

public class DelayRemover extends Module {
    // i dont think this works tbh
    public static ModuleDesc moduleDesc;
    private Field l = null;

    public DelayRemover() {
        super("Delay Remover", Module.category.Combat, 0);
        this.registerSetting(moduleDesc = new ModuleDesc("Gives you 1.7 hitreg."));
    }

    public void onEnable() {
        try {
            this.l = Minecraft.class.getDeclaredField("leftClickCounter");
        } catch (Exception ignored) {
        }

        if (this.l != null) {
            this.l.setAccessible(true);
        } else {
            this.disable();
        }

    }

    @SubscribeEvent
    public void onTick(TickEvent tickEvent) {
        if (GameUtils.isPlayerInGame() && this.l != null) {
            if (!mc.inGameHasFocus || mc.thePlayer.capabilities.isCreativeMode) {
                return;
            }

            try {
                this.l.set(mc, 0);
            } catch (IllegalAccessException | IndexOutOfBoundsException ignored) {
            }
        }

    }
}
