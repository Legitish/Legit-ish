package legitish.module.modules.combat;

import legitish.events.Subscribe;
import legitish.events.impl.MouseEvent;
import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleSliderSetting;
import legitish.module.modulesettings.impl.ModuleTickSetting;
import legitish.utils.GameUtils;
import legitish.utils.MouseUtils;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ClickAssist extends Module {
    public static ModuleSliderSetting chance;
    public static ModuleTickSetting leftClick, rightClick, blocksOnly, weaponOnly, onlyTargeting, cpsMin;
    private Robot robot;
    private boolean ignNL = false;
    private boolean ignNR = false;

    public ClickAssist() {
        super("ClickAssist", category.Combat, 0);
        this.registerSetting(new ModuleDesc("Boosts your CPS."));
        this.registerSetting(chance = new ModuleSliderSetting("Chance", 80.0D, 0.0D, 100.0D, 1.0D));
        this.registerSetting(leftClick = new ModuleTickSetting("Left click", true));
        this.registerSetting(weaponOnly = new ModuleTickSetting("Weapon only", true));
        this.registerSetting(onlyTargeting = new ModuleTickSetting("Only while targeting", false));
        this.registerSetting(rightClick = new ModuleTickSetting("Right click", false));
        this.registerSetting(blocksOnly = new ModuleTickSetting("Blocks only", true));
        this.registerSetting(cpsMin = new ModuleTickSetting("Above 5 cps", false));
    }

    public void onEnable() {
        try {
            this.robot = new Robot();
        } catch (AWTException var2) {
            this.disable();
        }
    }

    public void onDisable() {
        this.ignNL = false;
        this.ignNR = false;
        this.robot = null;
    }

    @SuppressWarnings("unused")
    @Subscribe(eventClass = MouseEvent.class)
    public void onMouseUpdate(MouseEvent ev) {
        if (chance.getInput() != 0.0D && GameUtils.isPlayerInGame()) {
            if (mc.currentScreen == null && !mc.thePlayer.isEating()) {
                double ch;
                if (ev.button == MouseEvent.Button.LEFT && leftClick.isToggled()) {
                    if (this.ignNL) {
                        this.ignNL = false;
                    } else {
                        if (weaponOnly.isToggled() && !GameUtils.getWeapon()) {
                            return;
                        }

                        if (onlyTargeting.isToggled() && (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null)) {
                            return;
                        }

                        if (cpsMin.isToggled() && MouseUtils.v() <= 5) {
                            this.fix(1);
                            return;
                        }

                        if (chance.getInput() != 100.0D) {
                            ch = Math.random();
                            if (ch >= chance.getInput() / 100.0D) {
                                this.fix(0);
                                return;
                            }
                        }

                        this.robot.mouseRelease(16);
                        this.robot.mousePress(16);
                        this.ignNL = true;
                    }
                } else if (ev.button == MouseEvent.Button.RIGHT && rightClick.isToggled()) {
                    if (this.ignNR) {
                        this.ignNR = false;
                    } else {
                        if (blocksOnly.isToggled()) {
                            ItemStack item = mc.thePlayer.getHeldItem();
                            if (item == null || !(item.getItem() instanceof ItemBlock)) {
                                this.fix(1);
                                return;
                            }
                        }

                        if (cpsMin.isToggled() && MouseUtils.i() <= 5) {
                            this.fix(1);
                            return;
                        }

                        if (chance.getInput() != 100.0D) {
                            ch = Math.random();
                            if (ch >= chance.getInput() / 100.0D) {
                                this.fix(1);
                                return;
                            }
                        }

                        this.robot.mouseRelease(4);
                        this.robot.mousePress(4);
                        this.ignNR = true;
                    }
                }

            }
            this.fix(0);
            this.fix(1);
        }
    }

    private void fix(int button) {
        if (button == 0) {
            if (this.ignNL && !Mouse.isButtonDown(0)) {
                this.robot.mouseRelease(16);
            }
        } else if (button == 1 && this.ignNR && !Mouse.isButtonDown(1)) {
            this.robot.mouseRelease(4);
        }

    }
}
