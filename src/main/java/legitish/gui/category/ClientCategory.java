package legitish.gui.category;

import legitish.gui.ClickGui;
import legitish.main.Legitish;
import legitish.utils.ColorUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.StencilUtils;

public class ClientCategory extends Category {
    public ClientCategory() {
        super("Client");
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ClickGui clickGUI = Legitish.clickGui;

        FontUtils.regular20.drawString("soon™", clickGUI.getX() + (clickGUI.getWidth() - 90) / 2, clickGUI.getY() + (clickGUI.getHeight() - 25) / 2, MinecraftFontRenderer.CenterMode.XY, false, ColorUtils.getFontColor(2).getRGB());
        StencilUtils.disableStencilBuffer();
        FontUtils.regular20.drawString("Client", clickGUI.getX() + 95, clickGUI.getY() + 8, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }
}
