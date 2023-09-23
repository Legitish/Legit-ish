package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.LivingUpdateEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.input.Keyboard;

public class JumpReset extends Module {

    public static ModuleSliderSetting chance, jumpDelay;
    public static ModuleTickSetting onlyWhileTargeting, holdingS;

    public JumpReset() {
        super("Jump Reset", category.Combat, 0);
        this.registerSetting(chance = new ModuleSliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(jumpDelay = new ModuleSliderSetting("Jump Delay", 20.0D, 0.0D, 40.0D, 1.0D));
        this.registerSetting(onlyWhileTargeting = new ModuleTickSetting("Only while targeting", false));
        this.registerSetting(holdingS = new ModuleTickSetting("Only moving", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = LivingUpdateEvent.class)
    public void onUpdate(LivingUpdateEvent event) {
        if (GameUtils.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && event.type == LivingUpdateEvent.Type.PRE) {
            for (PotionEffect effect : mc.thePlayer.getActivePotionEffects()) {
                String potion = effect.getEffectName();
                if (potion.equals(Potion.jump.getName())) {
                    return;
                }
            }

            if (onlyWhileTargeting.isToggled() && (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null)) {
                return;
            }

            if (holdingS.isToggled() && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                return;
            }

            if (chance.getInput() != 100.0D) {
                double ch = Math.random();
                if (ch >= chance.getInput() / 100.0D) {
                    return;
                }
            }

            if (!Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                int key = mc.gameSettings.keyBindJump.getKeyCode();
                KeyBinding.setKeyBindState(key, true);
                KeyBinding.onTick(key);
                javax.swing.Timer timer = new javax.swing.Timer((int) jumpDelay.getInput(), actionevent -> KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false));
                timer.setRepeats(false);
                timer.start();
            }
        }
    }


}
