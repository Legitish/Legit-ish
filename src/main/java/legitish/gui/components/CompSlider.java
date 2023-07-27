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
    private double width;
    private boolean dragging = false;

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
        width = clickGui.getWidth() - x - 5;

        double renderWidthMin = (width) * (sliderSetting.getInput() - min) / (max - min);

        animation.setAnimation((float) renderWidthMin, 14);

        double diff = Math.min(width, Math.max(0, mouseX - (clickGui.getX() + x)));

        if (dragging) {
            if (diff == 0) {
                sliderSetting.setValue(sliderSetting.getMin());
            } else {
                double newValue = MathUtils.round(((diff / width) * (max - min) + min), 2);
                sliderSetting.setValue(newValue);
            }
        }

        RRectUtils.drawGradientRoundCorner(clickGui.getX() + x, clickGui.getY() + y + 13, width, 2, 1);
        RRectUtils.drawRound(clickGui.getX() + x + animation.getValue(), clickGui.getY() + y + 13, width - animation.getValue(), 2, 1, ColorUtils.getFontColor(2));
        RRectUtils.drawGradientRoundCorner(clickGui.getX() + x + animation.getValue() - 2, clickGui.getY() + y + 10.75, 6, 6, 2.5);

        FontUtils.regular20.drawString(sliderSetting.get() + ": " + sliderSetting.getInput(), clickGui.getX() + x, clickGui.getY() + y, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGui.getX() + x - 2, clickGui.getY() + y + 10, width + 4, 10) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }
}
