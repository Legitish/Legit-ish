package legitish.gui.category.impl;

import legitish.gui.ClickGui;
import legitish.gui.category.Category;
import legitish.main.Legitish;
import legitish.utils.ColorUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.StencilUtils;

public class ConfigCategory extends Category {
    public ConfigCategory() {
        super("Config");
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClickGui clickGUI = Legitish.clickGui;

        FontUtils.regular20.drawString("Coming soon", clickGUI.getX() + 95, clickGUI.getY() + 30, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
        StencilUtils.disableStencilBuffer();
        FontUtils.regular20.drawString("Client", clickGUI.getX() + 95, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }
}
