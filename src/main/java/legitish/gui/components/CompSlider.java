package legitish.gui.components;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.utils.AnimationUtils;
import legitish.utils.ColorUtils;
import legitish.utils.MathUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;

public class CompSlider extends Comp {

    private final AnimationUtils animation = new AnimationUtils(0.0F);
    private final ModuleSliderSetting sliderSetting;
    private boolean dragging = false;
    private double renderWidth;
    private double renderWidth2;

    public CompSlider(double x, double y, Module mod, ModuleSliderSetting sliderSetting) {
        this.x = x;
        this.y = y;
        this.module = mod;
        this.sliderSetting = sliderSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY) {
        super.drawScreen(mouseX, mouseY, scrollY);

        double min = sliderSetting.getMin();
        double max = sliderSetting.getMax();
        double l = 90;

        renderWidth = (l) * (sliderSetting.getInput() - min) / (max - min);
        renderWidth2 = (l) * (sliderSetting.getMax() - min) / (max - min);

        animation.setAnimation((float) renderWidth, 14);

        double diff = Math.min(l, Math.max(0, mouseX - (clickGui.getX() + x - 70)));

        if (dragging) {
            if (diff == 0) {
                sliderSetting.setValue(sliderSetting.getMin());
            } else {
                double newValue = MathUtils.round(((diff / l) * (max - min) + min), 2);
                sliderSetting.setValue(newValue);
            }
        }

        RRectUtils.drawRound((float) (clickGui.getX() + x - 70), (float) (clickGui.getY() + y + 13), (float) (renderWidth2), 6, 3, ColorUtils.getBackgroundColor(2));
        RRectUtils.drawGradientRoundCorner((float) (clickGui.getX() + x - 70), (float) (clickGui.getY() + y + 13), animation.getValue(), 6, 3);
        RRectUtils.drawRound((float) (clickGui.getX() + x) + animation.getValue() - 72, (float) (clickGui.getY() + y + 10), 5, 12, 2, ColorUtils.getBackgroundColor(1));

        FontUtils.regular20.drawString(sliderSetting.get() + ": " + sliderSetting.getInput(), clickGui.getX() + x - 70, clickGui.getY() + y, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGui.getX() + x - 70, clickGui.getY() + y + 10, renderWidth2 + 3, 10) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }
}
