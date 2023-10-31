package legitish.module.modules.movement;

import legitish.events.Subscribe;
import legitish.events.impl.SlowdownEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;

public class NoSlow extends Module {
    public static ModuleSliderSetting speed;
    public static ModuleTickSetting noweapons, noconsumables;

    public NoSlow() {
        super("NoSlow", category.Movement, 0);
        this.registerSetting(new ModuleDesc("Reduces slowdown from certain actions."));
        this.registerSetting(speed = new ModuleSliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
        this.registerSetting(noweapons = new ModuleTickSetting("Blacklist Weapons", false));
        this.registerSetting(noconsumables = new ModuleTickSetting("Blacklist Consumables", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = SlowdownEvent.class)
    public static void noSlow(SlowdownEvent event) {
        if (!GameUtils.isPlayerInGame()) return;
        if (noweapons.isToggled() && GameUtils.getWeapon()) return;
        if (noconsumables.isToggled() && consumableCheck()) return;
        event.setCancelled(true);
        float val = (100.0F - (float) speed.getInput()) / 100.0F;
        mc.thePlayer.movementInput.moveStrafe *= val;
        mc.thePlayer.movementInput.moveForward *= val;
    }
    public static boolean consumableCheck() {
        if (mc.thePlayer.getHeldItem() != null) {
            return noconsumables.isToggled() && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion);
        } else return false;
    }
}