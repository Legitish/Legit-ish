package legitish.gui.components;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleComboSetting;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;

public class CompCombo extends Comp {
    private final ModuleComboSetting comboSetting;
    public int modeIndex;

    public CompCombo(double x, double y, Module mod, ModuleComboSetting comboSetting) {
        this.x = x;
        this.y = y;
        this.module = mod;
        this.comboSetting = comboSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY) {
        super.drawScreen(mouseX, mouseY, scrollY);

        RRectUtils.drawRound(clickGui.getX() + x, clickGui.getY() + y, FontUtils.regular20.getStringWidth(comboSetting.get() + ": " + comboSetting.getValue()) + 5, 11, 3, ColorUtils.getBackgroundColor(2));
        FontUtils.regular20.drawString(comboSetting.get() + ": " + comboSetting.getValue(), clickGui.getX() + x - 68, clickGui.getY() + y + 2, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGui.getX() + x, clickGui.getY() + y, 70, 10) && mouseButton == 0) {
            int max = comboSetting.getOptions().size();
            if (modeIndex + 1 >= max) {
                modeIndex = 0;
            } else {
                modeIndex++;
            }
            comboSetting.setValue(comboSetting.getOptions().get(modeIndex));
        }
    }
}
