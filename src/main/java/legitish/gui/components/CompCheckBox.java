package legitish.gui.components;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.AnimationUtils;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.GLUtils;
import legitish.utils.render.RRectUtils;
import net.minecraft.client.renderer.GlStateManager;

public class CompCheckBox extends Comp {

    private final AnimationUtils animation = new AnimationUtils(0.0F);
    private final AnimationUtils animation2 = new AnimationUtils(0.0F);
    private final ModuleTickSetting tickSetting;

    public CompCheckBox(double x, double y, Module module, ModuleTickSetting tickSetting) {
        this.x = x;
        this.y = y;
        this.module = module;
        this.tickSetting = tickSetting;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY) {
        animation.setAnimation(tickSetting.isToggled() ? 1 : 0, 10);
        animation2.setAnimation(tickSetting.isToggled() ? 255 : 0, 12);

        RRectUtils.drawRound((float) (clickGui.getX() + x - 70), (float) (clickGui.getY() + y), 10, 10, 3, ColorUtils.getBackgroundColor(2));

        GLUtils.startScale((float) (clickGui.getX() + x - 70 + clickGui.getX() + x - 70 + 10) / 2, (float) (clickGui.getY() + y + clickGui.getY() + y + 10) / 2, (float) animation.getValue());
        RRectUtils.drawGradientRoundCorner((float) (clickGui.getX() + x - 70), (float) (clickGui.getY() + y), 10, 10, 3);
        FontUtils.icon20.drawString("D", (clickGui.getX() + x - 70), (clickGui.getY() + y + 3), MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
        GlStateManager.popMatrix();

        FontUtils.regular20.drawString(tickSetting.get(), (int) (clickGui.getX() + x - 55), (clickGui.getY() + y + 2), MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (MouseUtils.mouseInBounds(mouseX, mouseY, clickGui.getX() + x - 70, clickGui.getY() + y, 10, 10) && mouseButton == 0) {
            tickSetting.setEnabled(!tickSetting.isToggled());
        }
    }

}
