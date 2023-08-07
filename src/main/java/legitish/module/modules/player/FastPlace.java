package legitish.module.modules.player;

import legitish.events.Subscribe;
import legitish.events.impl.ClientTickEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;

public class FastPlace extends Module {
    public static ModuleDesc desc;
    public static ModuleSliderSetting delay;
    public static ModuleTickSetting blocksOnly;
    public static Field field = null;

    public FastPlace() {
        super("Fast Place", category.Player, 0);
        this.registerSetting(desc = new ModuleDesc("Places blocks faster."));
        this.registerSetting(delay = new ModuleSliderSetting("Delay", 0.0D, 0.0D, 4.0D, 1.0D));
        this.registerSetting(blocksOnly = new ModuleTickSetting("Blocks only", true));
        try {
            field = mc.getClass().getDeclaredField("rightClickDelayTimer");
        } catch (Exception ignored) {
        }

        if (field != null) {
            field.setAccessible(true);
        }
    }

    public void onEnable() {
        if (field == null) {
            this.disable();
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(eventType = ClientTickEvent.class)
    public void onTick(ClientTickEvent event) {
        if (GameUtils.isPlayerInGame() && mc.inGameHasFocus && field != null) {
            if (blocksOnly.isToggled()) {
                ItemStack item = mc.thePlayer.getHeldItem();
                if (item == null || !(item.getItem() instanceof ItemBlock)) {
                    return;
                }
            }

            try {
                int delay = (int) FastPlace.delay.getInput();
                if (delay == 0) {
                    field.set(mc, 0);
                } else {
                    if (delay == 4) {
                        return;
                    }

                    int d = field.getInt(mc);
                    if (d == 4) {
                        field.set(mc, delay);
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
