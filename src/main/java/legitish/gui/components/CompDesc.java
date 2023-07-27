package legitish.gui.components;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleDesc;
import legitish.utils.AnimationUtils;
import legitish.utils.ColorUtils;
import legitish.utils.GameUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;

public class CompDesc extends Comp {
    private final ModuleDesc desc;

    public CompDesc(double x, double y, Module module, ModuleDesc desc) {
        this.x = x;
        this.y = y;
        this.module = module;
        this.desc = desc;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, double scrollY) {
        FontUtils.regular20.wrapText(desc.get(), clickGui.getX() + x, clickGui.getY() + y, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB(), clickGui.width - this.x - 5);
    }
}
