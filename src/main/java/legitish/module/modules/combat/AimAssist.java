package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.PlayerTickEvent;
import legitish.module.Module;
import legitish.module.modules.client.Targets;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

public class AimAssist extends Module {
    public static ModuleSliderSetting speed, fov, distance;
    public static ModuleTickSetting clickAim, weaponOnly, aimInvis, blatantMode;

    public AimAssist() {
        super("Aim Assist", category.Combat, 0);
        this.registerSetting(new ModuleDesc("Aims at enemies."));
        this.registerSetting(speed = new ModuleSliderSetting("Speed", 45.0D, 1.0D, 100.0D, 1.0D));
        this.registerSetting(fov = new ModuleSliderSetting("FOV", 90.0D, 15.0D, 180.0D, 1.0D));
        this.registerSetting(distance = new ModuleSliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
        this.registerSetting(clickAim = new ModuleTickSetting("Clicking only", true));
        this.registerSetting(weaponOnly = new ModuleTickSetting("Weapon only", false));
        this.registerSetting(aimInvis = new ModuleTickSetting("Aim at invis", false));
        this.registerSetting(blatantMode = new ModuleTickSetting("Blatant mode", false));
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = PlayerTickEvent.class)
    public void aim(PlayerTickEvent event) {
        if (mc.currentScreen == null && mc.inGameHasFocus) {
            if (!weaponOnly.isToggled() || GameUtils.getWeapon()) {
                if (!clickAim.isToggled() || (AutoClicker.leftClick.isToggled() && Mouse.isButtonDown(0))) {
                    Entity en = this.getEnemy();
                    if (en != null) {
                        if (blatantMode.isToggled()) {
                            GameUtils.aim(en, 0.0F);
                        } else {
                            double n = GameUtils.n(en);
                            if (n > 1.0D || n < -1.0D) {
                                float val = (float) (-(n / (101.0D - speed.getInput())));
                                mc.thePlayer.rotationYaw += val;
                            }
                        }
                    }
                }
            }
        }
    }

    public Entity getEnemy() {
        int fov = (int) AimAssist.fov.getInput();
        for (EntityPlayer en : mc.theWorld.playerEntities) {
            if (!Targets.isTarget(en)) {
                continue;
            } else if (!aimInvis.isToggled() && en.isInvisible()) {
                continue;
            } else if ((double) mc.thePlayer.getDistanceToEntity(en) > distance.getInput()) {
                continue;
            } else if (!blatantMode.isToggled() && !GameUtils.fov(en, (float) fov)) {
                continue;
            }
            return en;
        }
        return null;
    }
}
