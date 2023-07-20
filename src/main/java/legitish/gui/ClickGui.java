package legitish.gui;

import legitish.gui.category.Category;
import legitish.gui.category.CategoryManager;
import legitish.gui.category.ModulesCategory;
import legitish.gui.category.SettingsCategory;
import legitish.main.Legitish;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.GaussianBlur;
import legitish.utils.render.RenderUtils;
import legitish.utils.render.RoundedUtils;
import legitish.utils.render.StencilUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ClickGui extends GuiScreen {
    private final int width = 305;
    private final int height = 230;
    public boolean closed;
    public Category selectedCategory;
    public CategoryManager categoryManager;
    private double x = 100;
    private double y = 100;
    private int currentCategoryY;

    public ClickGui() {
        categoryManager = new CategoryManager();
        selectedCategory = categoryManager.getCategoryByClass(ModulesCategory.class);
    }

    public void initMain() {
        closed = false;

        for (Category c : categoryManager.getCategories()) {
            c.initGui();
        }
    }

    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.x = ((double) sr.getScaledWidth() / 2) - (this.getWidth() / 2);
        this.y = ((double) sr.getScaledHeight() / 2) - (this.getHeight() / 2);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int categoryOffset = 35;

        if (closed) {
            mc.displayGuiScreen(null);
        }
        GaussianBlur.renderBlur(10);
        RenderUtils.startScale((float) ((this.getX()) + (this.getX() + this.getWidth())) / 2, (float) ((this.getY()) + (this.getY() + this.getHeight())) / 2, 1);
        if (ModulesCategory.openModSetting) {
            RoundedUtils.drawRound(this.getX() + 90, this.getY() + 25, this.getWidth() - 90D, this.getHeight() - 25, 3, ColorUtils.getBackgroundColor(4));
        } else {
            RoundedUtils.drawGradientRoundCorner(this.getX() + 90, this.getY() + 25, this.getWidth() - 90D, this.getHeight() - 25, 3, ColorUtils.getBackgroundColor(2), ColorUtils.getBackgroundColor(1));
        }
        RoundedUtils.drawRound(this.getX() + 90, this.getY(), this.getWidth() - 90, 20, 3, ColorUtils.getBackgroundColor(4));
        RoundedUtils.drawRound(this.getX(), this.getY(), 85, this.getHeight(), 3, ColorUtils.getBackgroundColor(4));

        FontUtils.regular_bold26.drawString("LEGITISH", this.getX() + 9D, this.getY() + 6D, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(1).getRGB());

        RoundedUtils.drawGradientRoundCorner(this.getX() + 5D, this.getY() + currentCategoryY, 75, 15, 4, ColorUtils.getBackgroundColor(2), ColorUtils.getBackgroundColor(1));

        for (Category category : categoryManager.getCategories()) {
            boolean settingsCategory = selectedCategory.equals(categoryManager.getCategoryByClass(SettingsCategory.class));

            float addX = !settingsCategory ? 88 : 95;

            if (!category.equals(categoryManager.getCategoryByClass(SettingsCategory.class))) {
                FontUtils.regular20.drawString(category.getName(), (int) this.getX() + 12, (int) this.getY() + categoryOffset + 5, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
            } else {
                RoundedUtils.drawRound(this.getX(), this.getY() + this.getHeight() - 26, 85, 1, 0, ColorUtils.getBackgroundColor(2));
                FontUtils.icon20.drawString("I", (int) this.getX() + 12, (int) (this.getY() + this.getHeight()) - 14, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
                FontUtils.regular20.drawString(category.getName(), this.getX() + 26, (this.getY() + this.getHeight()) - 15, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
            }

            StencilUtils.initStencilToWrite();
            RoundedUtils.drawRound((float) this.getX() + addX, (float) this.getY() + 25D, this.getWidth() - addX, this.getHeight() - 25D, 6D, ColorUtils.getBackgroundColor(1));
            StencilUtils.readStencilBuffer(1);

            if (category.equals(selectedCategory)) {
                if (!category.equals(categoryManager.getCategoryByClass(SettingsCategory.class))) {
                    currentCategoryY = categoryOffset;
                }
                category.drawScreen(mouseX, mouseY, partialTicks);
            }

            StencilUtils.uninitStencilBuffer();

            categoryOffset += 25;
        }

        //this.drawModDescription(mouseX, mouseY);
        GlStateManager.popMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int categoryOffset = 35;

        for (Category c : categoryManager.getCategories()) {
            if (!c.equals(categoryManager.getCategoryByClass(SettingsCategory.class))) {
                if (MouseUtils.isInside(mouseX, mouseY, this.getX() + 4, this.getY() + categoryOffset, 75, 16) && mouseButton == 0) {
                    selectedCategory = c;
                    ModulesCategory.openModSetting = false;
                }
            } else {
                if (MouseUtils.isInside(mouseX, mouseY, this.getX(), this.getY() + height - 30, 85, 30) && mouseButton == 0) {
                    selectedCategory = c;
                    ModulesCategory.openModSetting = false;
                }
            }

            if (c.equals(selectedCategory)) {
                c.mouseClicked(mouseX, mouseY, mouseButton);
            }
            categoryOffset += 25;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Category c : categoryManager.getCategories()) {
            if (c.equals(selectedCategory)) {
                c.mouseReleased(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Category c : categoryManager.getCategories()) {
            if (c.equals(selectedCategory)) {
                c.keyTyped(typedChar, keyCode);
            }
        }
        if (keyCode == 1) {
            if (ModulesCategory.openModSetting) {
                ModulesCategory.openModSetting = false;
            } else {
                closed = true;
            }
        }
    }

    public void onGuiClosed() {
        ModulesCategory.openModSetting = false;
        Legitish.configManager.save();
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

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
