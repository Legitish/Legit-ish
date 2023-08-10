package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.PlayerTickEvent;
import legitish.events.impl.SwingEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleDoubleSliderSetting;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.utils.CooldownUtils;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Mouse;

public class AutoBlock extends Module {
    public static ModuleDoubleSliderSetting distance;
    public static ModuleSliderSetting chance;
    private boolean engaged;
    private CooldownUtils engagedTime = new CooldownUtils(0);

    public AutoBlock(){
        super("AutoBlock", category.Combat, 0);
        this.registerSetting(new ModuleDesc("Blocks when enemies are nearby."));
        this.registerSetting(distance = new ModuleDoubleSliderSetting("Distance to player (blocks)", 0, 3, 0, 6, 0.01));
        this.registerSetting(chance = new ModuleSliderSetting("Chance %", 100, 0, 100, 1));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = PlayerTickEvent.class)
    public void onTick(PlayerTickEvent event){
        if(!GameUtils.isPlayerInGame() || !GameUtils.getWeapon() ) {
            return;
        }

        if (mc.objectMouseOver != null && mc.objectMouseOver.entityHit != null && mc.thePlayer.getDistanceToEntity(mc.objectMouseOver.entityHit) >= distance.getInputMin() && mc.thePlayer.getDistanceToEntity(mc.objectMouseOver.entityHit) <= distance.getInputMax() && (chance.getInput() == 100 || Math.random() <= chance.getInput() / 100)){
            engaged = true;
            press();
        } else if (!Mouse.isButtonDown(1) && engaged) {
            release();
            engaged = false;
        }
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = SwingEvent.class)
    public void onSwing(SwingEvent event){
        if (engaged) {
            if (event.type == SwingEvent.Type.PRE) {
                release();
            } else if (event.type == SwingEvent.Type.POST) {
                press();
            }
        }
    }

    private static void release() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, false);
        MouseUtils.sendClick(1, false);
    }

    private static void press() {
        int key = mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(key, true);
        //KeyBinding.onTick(key);
        MouseUtils.sendClick(1, true);
    }
}
