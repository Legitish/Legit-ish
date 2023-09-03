package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.LivingUpdateEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.utils.GameUtils;
import net.minecraft.client.settings.KeyBinding;

public class JumpReset extends Module {

    public static ModuleSliderSetting chance;

    public JumpReset() {
        super("Jump Reset", category.Combat, 0);
        this.registerSetting(chance = new ModuleSliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
    }

    @Subscribe(eventClass = LivingUpdateEvent.class)
    public void onTick(LivingUpdateEvent event) {
        if (GameUtils.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && event.type == LivingUpdateEvent.Type.PRE) {
            if (chance.getInput() != 100.0D) {
                double ch = Math.random();
                if (ch >= chance.getInput() / 100.0D) {
                    return;
                }
            }

            int key = mc.gameSettings.keyBindJump.getKeyCode();
            KeyBinding.setKeyBindState(key, true);
            KeyBinding.onTick(key);
            javax.swing.Timer timer = new javax.swing.Timer(20, actionevent -> KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false));
            timer.setRepeats(false);
            timer.start();
        }
    }


}
