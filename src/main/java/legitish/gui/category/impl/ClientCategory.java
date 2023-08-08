package legitish.gui.category.impl;

import legitish.gui.ClickGui;
import legitish.gui.category.Category;
import legitish.gui.components.*;
import legitish.main.Legitish;
import legitish.module.Module;
import legitish.module.modulesettings.ModuleSettingsList;
import legitish.module.modulesettings.impl.*;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;
import legitish.utils.render.StencilUtils;

import java.util.ArrayList;

public class ClientCategory extends Category {
    public final ArrayList<Comp> comps = new ArrayList<>();
    public final ArrayList<Comp> bindComps = new ArrayList<>();
    private Module selectedMod;
    private boolean canToggle;

    public ClientCategory() {
        super("Client");
    }

    public void initGui() {
        ClickGui clickGUI = Legitish.clickGui;
        int offset = 30;
        for (Module module : Legitish.moduleManager.inCategory(Module.category.Client)) {
            bindComps.add(new CompBind(clickGUI.getX() + clickGUI.getWidth() - 26, clickGUI.getY() + offset, module));
            offset += 25;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int offset = 30;
        ClickGui clickGUI = Legitish.clickGui;
        if (!clickGUI.openModSetting) {
            for (Module module : Legitish.moduleManager.inCategory(Module.category.Client)) {
                RRectUtils.drawRound(clickGUI.getX() + 95, clickGUI.getY() + offset, clickGUI.getWidth() - 100, 20, 3, ColorUtils.getBackgroundColor(4));
                FontUtils.regular20.drawString(module.getName(), clickGUI.getX() + 105, clickGUI.getY() + offset + 8, MinecraftFontRenderer.CenterMode.NONE, false, module.isEnabled() ? ColorUtils.getBackgroundColor(1).getRGB() : ColorUtils.getFontColor(2).getRGB());
                if (!module.getSettings().isEmpty()) {
                    FontUtils.icon20.drawString("C", clickGUI.getX() + clickGUI.getWidth() - 21, clickGUI.getY() + offset + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
                }
                offset += 25;
            }
            for (Comp comp : bindComps) {
                comp.drawScreen(mouseX, mouseY, 0);
            }
        } else if (selectedMod != null) {
            for (Comp comp : comps) {
                comp.drawScreen(mouseX, mouseY, 0);
            }
        }

        canToggle = MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX(), clickGUI.getY(), clickGUI.getWidth(), clickGUI.getHeight());
        StencilUtils.disableStencilBuffer();

        FontUtils.regular20.drawString("Client", clickGUI.getX() + 95, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int offset = 30;
        ClickGui clickGUI = Legitish.clickGui;
        for (Comp comp : bindComps) {
            comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
        for (Module mod : Legitish.moduleManager.inCategory(Module.category.Client)) {
            if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX() + clickGUI.getWidth() - 23.5, clickGUI.getY() + offset, 23.5, 20) && canToggle && !clickGUI.openModSetting && mouseButton == 0) {
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

            if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX() + 95, clickGUI.getY() + offset, clickGUI.getWidth() - 200, 26) && canToggle) {
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
}
