package legitish.gui.category;

import legitish.gui.ClickGui;
import legitish.gui.components.CompBind;
import legitish.main.Legitish;
import legitish.module.ModuleManager;
import legitish.utils.ColorUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.StencilUtils;


public class SettingsCategory extends Category {
    private CompBind compBind;

    public SettingsCategory() {
        super("Settings");
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClickGui clickGUI = Legitish.clickGui;
        FontUtils.regular20.drawString("soon™", clickGUI.getX() + clickGUI.getWidth() / 2 + 90, clickGUI.getY() + (clickGUI.getHeight() - 25) / 2, MinecraftFontRenderer.CenterMode.XY, false, ColorUtils.getFontColor(2).getRGB());
        compBind = new CompBind(clickGUI.getX() + clickGUI.getWidth() / 2 + 90, clickGUI.getY() + (clickGUI.getHeight() - 25) / 2, ModuleManager.gui);
        compBind.drawScreen(mouseX, mouseY, 0);
        StencilUtils.uninitStencilBuffer();
        FontUtils.regular20.drawString("Settings", clickGUI.getX() + 95, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        compBind.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
