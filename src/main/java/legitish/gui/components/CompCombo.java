package legitish.gui.components;

import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleComboSetting;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;

public class CompCombo extends Comp {
    private static final MinecraftFontRenderer mfr = FontUtils.regular20;
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
        RRectUtils.drawGradientRoundCorner(clickGui.getX() + x + mfr.getStringWidth(comboSetting.get() + " ") + 1, clickGui.getY() + y, mfr.getStringWidth(comboSetting.getValue()) + 1, mfr.getHeight() + 4, 3);
        mfr.drawString(comboSetting.get() + " " + comboSetting.getValue(), clickGui.getX() + x + 2, clickGui.getY() + y + 3, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGui.getX() + x, clickGui.getY() + y, mfr.getStringWidth(comboSetting.get() + " " + comboSetting.getValue()), mfr.getHeight()) && mouseButton == 0) {
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
