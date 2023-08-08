package legitish.gui.category.impl;

import legitish.gui.ClickGui;
import legitish.gui.category.Category;
import legitish.gui.components.*;
import legitish.main.Legitish;
import legitish.module.Module;
import legitish.module.modulesettings.ModuleSettingsList;
import legitish.module.modulesettings.impl.*;
import legitish.utils.AnimationUtils;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.MouseUtils.Scroll;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;
import legitish.utils.render.StencilUtils;

import java.util.ArrayList;

public class ModulesCategory extends Category {
    public static final AnimationUtils scrollAnimation = new AnimationUtils(0.0F);
    public static double scrollY;
    public final ArrayList<Comp> comps = new ArrayList<>();
    public final ArrayList<Comp> bindComps = new ArrayList<>();
    public Module.category moduleCategory;
    private Module selectedMod;
    private boolean canToggle;

    public ModulesCategory() {
        super("Modules");
    }

    @Override
    public void initGui() {
        setCategory(Module.category.Combat);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int offset = 30;
        ClickGui clickGUI = Legitish.clickGui;
        if (!clickGUI.openModSetting) {
            for (Module module : Legitish.moduleManager.inCategory(getModuleCategory())) {
                RRectUtils.drawRound(clickGUI.getX() + 95, clickGUI.getY() + offset + scrollAnimation.getValue(), clickGUI.getWidth() - 100, 20, 3, ColorUtils.getBackgroundColor(4));
                FontUtils.regular20.drawString(module.getName(), clickGUI.getX() + 105, clickGUI.getY() + offset + scrollAnimation.getValue() + 8, MinecraftFontRenderer.CenterMode.NONE, false, module.isEnabled() ? ColorUtils.getBackgroundColor(1).getRGB() : ColorUtils.getFontColor(2).getRGB());
                if (!module.getSettings().isEmpty()) {
                    FontUtils.icon20.drawString("C", clickGUI.getX() + clickGUI.getWidth() - 21, clickGUI.getY() + offset + scrollAnimation.getValue() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
                }
                offset += 25;
            }
            for (Comp comp : bindComps) {
                comp.drawScreen(mouseX, mouseY, scrollAnimation.getValue());
            }
        } else if (selectedMod != null) {
            for (Comp comp : comps) {
                comp.drawScreen(mouseX, mouseY, scrollAnimation.getValue());
            }
        }

        final Scroll scroll = MouseUtils.scroll();

        if (scroll != null && !clickGUI.openModSetting) {
            switch (scroll) {
                case DOWN:
                    if (scrollY > -((Legitish.moduleManager.inCategory(getModuleCategory()).size() - 8) * 25)) {
                        scrollY -= 25;
                    }
                    break;
                case UP:
                    if (scrollY < -10) {
                        scrollY += 25;
                    } else {
                        if (Legitish.moduleManager.inCategory(getModuleCategory()).size() > 8) {
                            scrollY = 0;
                        }
                    }
                    break;
            }
        }
        scrollAnimation.setAnimation(scrollY, 16);

        double addX = getModuleCategory() == Module.category.Movement ? 20 : getModuleCategory() == Module.category.Player ? 40 : getModuleCategory() == Module.category.Visual ? 60 : 0;
        canToggle = MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX(), clickGUI.getY(), clickGUI.getWidth(), clickGUI.getHeight());
        StencilUtils.disableStencilBuffer();

        FontUtils.regular20.drawString(clickGUI.openModSetting ? selectedMod.getName() : moduleCategory.name(), clickGUI.getX() + 95, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
        RRectUtils.drawGradientRoundCorner(clickGUI.getX() + clickGUI.getWidth() - 76 + addX, clickGUI.getY() + 4, 12, 12, 3);
        FontUtils.icon20.drawString("O", clickGUI.getX() + clickGUI.getWidth() - 75, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
        FontUtils.icon20.drawString("F", clickGUI.getX() + clickGUI.getWidth() - 55, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
        FontUtils.icon20.drawString("P", clickGUI.getX() + clickGUI.getWidth() - 35, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
        FontUtils.icon20.drawString("K", clickGUI.getX() + clickGUI.getWidth() - 15, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
        // I would separate these with spaces, but it causes way too many alignment problems
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int offset = 30;
        ClickGui clickGUI = Legitish.clickGui;
        for (Comp comp : bindComps) {
            comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX() + clickGUI.getWidth() - 78, clickGUI.getY(), 78, 20)) {
            if (mouseX <= clickGUI.getX() + clickGUI.getWidth() - 60D) {
                setCategory(Module.category.Combat);
            } else if (mouseX <= clickGUI.getX() + clickGUI.getWidth() - 40) {
                setCategory(Module.category.Movement);
            } else if (mouseX <= clickGUI.getX() + clickGUI.getWidth() - 20) {
                setCategory(Module.category.Player);
            } else if (mouseX <= clickGUI.getX() + clickGUI.getWidth()) {
                setCategory(Module.category.Visual);
            }
        }
        for (Module mod : Legitish.moduleManager.inCategory(getModuleCategory())) {
            if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX() + clickGUI.getWidth() - 23.5, clickGUI.getY() + offset + scrollAnimation.getValue(), 23.5, 20) && canToggle && !clickGUI.openModSetting && mouseButton == 0) {
                int settingOffset = 30;
                comps.clear();
                if (!mod.getSettings().isEmpty()) {
                    int alternate = 0;
                    for (ModuleSettingsList settingsList : mod.getSettings()) {
                        selectedMod = mod;
                        clickGUI.openModSetting = true;
                        if (!(settingsList instanceof ModuleTickSetting)) {
                            settingOffset += alternate == 1 ? 15 : 0;
                            alternate = alternate == 1 ? 0 : alternate;
                        }
                        if (settingsList instanceof ModuleDesc) {
                            ModuleDesc desc = (ModuleDesc) settingsList;
                            comps.add(new CompDesc(95, settingOffset, selectedMod, desc));
                            settingOffset += 20;
                        }
                        if (settingsList instanceof ModuleComboSetting) {
                            ModuleComboSetting comboSetting = (ModuleComboSetting) settingsList;
                            comps.add(new CompCombo(95, settingOffset, selectedMod, comboSetting));
                            settingOffset += 15;
                        }
                        if (settingsList instanceof ModuleTickSetting) {
                            ModuleTickSetting tickSetting = (ModuleTickSetting) settingsList;
                            if (alternate == 1) {
                                comps.add(new CompCheckBox(95 + (clickGUI.getWidth() - 95) / 2, settingOffset, selectedMod, tickSetting));
                                alternate = 0;
                                settingOffset += 15;
                            } else {
                                comps.add(new CompCheckBox(95, settingOffset, selectedMod, tickSetting));
                                alternate = 1;
                            }
                        }
                        if (settingsList instanceof ModuleSliderSetting) {
                            ModuleSliderSetting sliderSetting = (ModuleSliderSetting) settingsList;
                            comps.add(new CompSlider(95, settingOffset, selectedMod, sliderSetting));
                            settingOffset += 25;
                        }
                        if (settingsList instanceof ModuleDoubleSliderSetting) {
                            ModuleDoubleSliderSetting doubleSliderSetting = (ModuleDoubleSliderSetting) settingsList;
                            comps.add(new CompDoubleSlider(95, settingOffset, selectedMod, doubleSliderSetting));
                            settingOffset += 25;
                        }
                    }
                }
            }

            if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX() + 95, clickGUI.getY() + offset + scrollAnimation.getValue(), clickGUI.getWidth() - 200, 26) && canToggle) {
                if (mouseButton == 0 && !clickGUI.openModSetting) {
                    mod.toggle();
                }
            }
            offset += 25;
        }

        if (clickGUI.openModSetting) {
            for (Comp comp : comps) {
                comp.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Comp comp : comps) {
            comp.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        ClickGui clickGUI = Legitish.clickGui;
        if (!clickGUI.openModSetting) {
            for (Comp comp : bindComps) {
                comp.keyTyped(typedChar, keyCode);
            }
        }
    }

    public void setCategory(Module.category category) {
        ClickGui clickGUI = Legitish.clickGui;
        int offset = 30;
        moduleCategory = category;
        scrollY = 0;
        clickGUI.openModSetting = false;
        bindComps.clear();

        for (Module module : Legitish.moduleManager.inCategory(getModuleCategory())) {
            bindComps.add(new CompBind(clickGUI.getX() + clickGUI.getWidth() - 26, clickGUI.getY() + offset + scrollAnimation.getValue(), module));
            offset += 25;
        }
    }

    public Module.category getModuleCategory() {
        return moduleCategory;
    }
}
