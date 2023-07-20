package legitish.gui.category;

import net.minecraft.client.Minecraft;

public class Category {
    private final String name;
    public Minecraft mc = Minecraft.getMinecraft();

    public Category(String name) {
        this.name = name;
    }

    public void initGui() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public String getName() {
        return name;
    }
}
