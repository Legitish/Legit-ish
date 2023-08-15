package legitish.gui;

import legitish.main.Legitish;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class EditHUD extends GuiScreen {
    private double x = 100;
    private double y = 100;

    public EditHUD() {
    }

    public void initMain() {
    }

    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.x = ((double) sr.getScaledWidth() / 2);
        this.y = ((double) sr.getScaledHeight() / 2);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    public void onGuiClosed() {
        Legitish.configManager.saveConfig();
        Legitish.clientConfig.saveConfig();
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
