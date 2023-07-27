package legitish.module.modules.combat;

import legitish.module.Module;
import legitish.module.modules.render.Targets;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.GameUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

import java.util.Iterator;

public class AimAssist extends Module {
    public static ModuleSliderSetting speed, fov, distance;
    public static ModuleTickSetting clickAim, weaponOnly, aimInvis, blatantMode;

    public AimAssist() {
        super("Aim Assist", category.Combat, 0);
        this.registerSetting(speed = new ModuleSliderSetting("Speed", 45.0D, 1.0D, 100.0D, 1.0D));
        this.registerSetting(fov = new ModuleSliderSetting("FOV", 90.0D, 15.0D, 180.0D, 1.0D));
        this.registerSetting(distance = new ModuleSliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
        this.registerSetting(clickAim = new ModuleTickSetting("Click aim", true));
        this.registerSetting(weaponOnly = new ModuleTickSetting("Weapon only", false));
        this.registerSetting(aimInvis = new ModuleTickSetting("Aim invis", false));
        this.registerSetting(blatantMode = new ModuleTickSetting("Blatant mode", false));
    }

    public void update() {
        if (mc.currentScreen == null && mc.inGameHasFocus) {
            if (!weaponOnly.isToggled() || GameUtils.getWeapon()) {
                if (!clickAim.isToggled() || (AutoClicker.leftClick.isToggled() && Mouse.isButtonDown(0))) {
                    Entity en = this.getEnemy();
                    if (en != null) {
                        if (blatantMode.isToggled()) {
                            GameUtils.aim(en, 0.0F, false);
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
        Iterator<EntityPlayer> var2 = mc.theWorld.playerEntities.iterator();

        EntityPlayer en;
        do {
            do {
                do {
                    do {
                        do {
                            do {
                                if (!var2.hasNext()) {
                                    return null;
                                }

                                en = var2.next();
                            } while (en == mc.thePlayer);
                        } while (en.deathTime != 0);
                    } while (!aimInvis.isToggled() && en.isInvisible());
                } while ((double) mc.thePlayer.getDistanceToEntity(en) > distance.getInput());
            } while (Targets.bot(en));
        } while (!blatantMode.isToggled() && !GameUtils.fov(en, (float) fov));

        return en;
    }
}
