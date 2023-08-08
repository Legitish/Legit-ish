package legitish.gui.category.impl;

import legitish.gui.ClickGui;
import legitish.gui.category.Category;
import legitish.gui.components.*;
import legitish.main.Legitish;
import legitish.module.Module;
import legitish.module.ModuleManager;
import legitish.module.modulesettings.ModuleSettingsList;
import legitish.module.modulesettings.impl.*;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.StencilUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;


public class SettingsCategory extends Category {
    public final ArrayList<Comp> comps = new ArrayList<>();
    final MinecraftFontRenderer mfr = FontUtils.regular16;
    private boolean isBinding = false;
    private double width;

    public SettingsCategory() {
        super("Settings");
    }

    @Override
    public void initGui() {
        ClickGui clickGUI = Legitish.clickGui;
        comps.clear();
        int alternate = 0;
        int settingOffset = 30;
        Module selectedMod = ModuleManager.gui;
        for (ModuleSettingsList settingsList : selectedMod.getSettings()) {
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

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClickGui clickGUI = Legitish.clickGui;
        for (Comp comp : comps) {
            comp.drawScreen(mouseX, mouseY, 0);
        }
        final String bindText = this.isBinding ? "Press a key" : Keyboard.getKeyName(ModuleManager.gui.getKeycode()).equalsIgnoreCase("none") ? "-" : Keyboard.getKeyName((ModuleManager.gui.getKeycode()));
        width = mfr.getStringWidth(bindText) < 14 ? 14 : mfr.getStringWidth(bindText) + 4;
        mfr.drawString(bindText, clickGUI.getX() + 150 - width + width / 2 - 2, clickGUI.getY() + 40, MinecraftFontRenderer.CenterMode.X, false, ColorUtils.getFontColor(2).getRGB());

        StencilUtils.disableStencilBuffer();
        FontUtils.regular20.drawString("Settings", clickGUI.getX() + 95, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        ClickGui clickGUI = Legitish.clickGui;
        for (Comp comp : comps) {
            comp.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGUI.getX() + 95 - width + width / 2 - 2, clickGUI.getY() + 8, width + 5, 20) && mouseButton == 0) {
            this.isBinding = !this.isBinding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.isBinding) {
            if (keyCode == 11) {
                ModuleManager.gui.setbind(38);
            } else {
                ModuleManager.gui.setbind(keyCode);
            }
            this.isBinding = false;
        }
    }
}
