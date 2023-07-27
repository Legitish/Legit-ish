package legitish.gui.components;

import legitish.module.Module;
import legitish.module.modules.client.Gui;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.RRectUtils;
import org.lwjgl.input.Keyboard;

public class CompBind extends Comp {
    MinecraftFontRenderer mfr = FontUtils.regular16;
    private boolean isBinding = false;
    private double width;

    public CompBind(double x, double y, Module module) {
        this.x = x;
        this.y = y;
        this.module = module;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY) {
        final String bindText = this.isBinding ? "Press a key" : Keyboard.getKeyName(module.getKeycode()).equalsIgnoreCase("none") ? "-" : Keyboard.getKeyName(module.getKeycode());
        width = mfr.getStringWidth(bindText) < 14 ? 14 : mfr.getStringWidth(bindText) + 4;
        RRectUtils.drawRoundOutline(x - width - 2, y + scrollY + 3, width, 14, 3, 0.5, ColorUtils.getBackgroundColor(4), ColorUtils.getBackgroundColor(1));
        mfr.drawString(bindText, x - width + width / 2 - 2, y + scrollY + 9, MinecraftFontRenderer.CenterMode.X, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (MouseUtils.mouseInBounds(mouseX, mouseY, x - width - 4.5, y, width + 5, 20) && mouseButton == 0) {
            this.isBinding = !this.isBinding;
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (this.isBinding) {
            if (keyCode == 11) {
                if (this.module instanceof Gui) {
                    this.module.setbind(38);
                } else {
                    this.module.setbind(0);
                }
            } else {
                this.module.setbind(keyCode);
            }
            this.isBinding = false;
        }
    }
}
