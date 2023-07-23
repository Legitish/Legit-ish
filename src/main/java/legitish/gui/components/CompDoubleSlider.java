package legitish.gui.components;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleDoubleSliderSetting;
import legitish.utils.AnimationUtils;
import legitish.utils.ColorUtils;
import legitish.utils.MathUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;

public class CompDoubleSlider extends Comp {

    private final AnimationUtils animationMin = new AnimationUtils(0.0F);
    private final AnimationUtils animationMax = new AnimationUtils(0.0F);
    private final ModuleDoubleSliderSetting doubleSliderSetting;
    private boolean dragging = false;
    private double realMin;
    private double realMax;
    private int focus;

    public CompDoubleSlider(double x, double y, Module mod, ModuleDoubleSliderSetting doubleSliderSetting) {
        this.x = x;
        this.y = y;
        this.module = mod;
        this.doubleSliderSetting = doubleSliderSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY) {
        super.drawScreen(mouseX, mouseY, scrollY);
        double min = doubleSliderSetting.getMin();
        double max = doubleSliderSetting.getMax();
        double width = 90;

        realMin = (width) * (doubleSliderSetting.getInputMin() - min) / (max - min);
        realMax = (width) * (doubleSliderSetting.getInputMax() - min) / (max - min);

        animationMin.setAnimation((float) realMin, 14);
        animationMax.setAnimation((float) realMax, 14);
        double boundedMouseX = Math.min(width, Math.max(0, mouseX - (clickGui.getX() + x - 70)));
        double newValue = MathUtils.round((boundedMouseX / width) * (max - min) + min, 2);
        if (dragging) {
            if (boundedMouseX > realMin + (realMax - realMin) / 2 || focus == 2) {
                if (focus == 0) focus = 2;
                if (focus == 2) {
                    if (boundedMouseX <= realMin) {
                        doubleSliderSetting.setValueMax(doubleSliderSetting.getInputMin());
                    } else {
                        doubleSliderSetting.setValueMax(newValue);
                    }
                }
            }

            if (boundedMouseX < realMin + (realMax - realMin) / 2 || focus == 1) {
                if (focus == 0) focus = 1;
                if (focus == 1) {
                    if (boundedMouseX >= realMax) {
                        doubleSliderSetting.setValueMin(doubleSliderSetting.getInputMax());
                    } else {
                        doubleSliderSetting.setValueMin(newValue);
                    }
                }
            }
        } else {
            if (focus != 0) focus = 0;
        }

        RRectUtils.drawRound((float) (clickGui.getX() + x - 70), (float) (clickGui.getY() + y + 13), (float) (width), 6, 3, ColorUtils.getBackgroundColor(2));
        RRectUtils.drawGradientRoundCorner((float) (clickGui.getX() + x - 70 + animationMin.getValue()), (float) (clickGui.getY() + y + 13), animationMax.getValue() - animationMin.getValue(), 6, 3);
        RRectUtils.drawRound((float) (clickGui.getX() + x) + animationMin.getValue() - 72, (float) (clickGui.getY() + y + 10), 5, 12, 2, ColorUtils.getBackgroundColor(1));
        RRectUtils.drawRound((float) (clickGui.getX() + x) + animationMax.getValue() - 72, (float) (clickGui.getY() + y + 10), 5, 12, 2, ColorUtils.getBackgroundColor(1));

        FontUtils.regular20.drawString(doubleSliderSetting.get() + ": " + doubleSliderSetting.getInputMin() + ", " + doubleSliderSetting.getInputMax(), clickGui.getX() + x - 70, clickGui.getY() + y, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGui.getX() + x - 70, clickGui.getY() + y + 10, realMax + 3, 10) && mouseButton == 0) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        dragging = false;
    }
}
