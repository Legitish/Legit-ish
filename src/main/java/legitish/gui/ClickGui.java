package legitish.gui;

import legitish.gui.category.Category;
import legitish.gui.category.CategoryManager;
import legitish.gui.category.impl.ModulesCategory;
import legitish.gui.category.impl.SettingsCategory;
import legitish.main.Legitish;
import legitish.utils.ColorUtils;
import legitish.utils.MouseUtils;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;
import legitish.utils.render.GLUtils;
import legitish.utils.render.RRectUtils;
import legitish.utils.render.StencilUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ClickGui extends GuiScreen {
    public final CategoryManager categoryManager;
    public Category selectedCategory;
    public boolean openModSetting = false;
    private double x = 100;
    private double y = 100;
    private int currentCategoryY;

    public ClickGui() {
        categoryManager = new CategoryManager();
        selectedCategory = categoryManager.getCategoryByClass(ModulesCategory.class);
    }

    public void initMain() {
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
        GLUtils.startScale((float) ((this.getX()) + (this.getX() + this.getWidth())) / 2, (float) ((this.getY()) + (this.getY() + this.getHeight())) / 2, 1);
        if (openModSetting) {
            RRectUtils.drawRound(this.getX() + 90, this.getY() + 25, this.getWidth() - 90D, this.getHeight() - 25, 3, ColorUtils.getBackgroundColor(4));
        } else {
            RRectUtils.drawGradientRoundCorner(this.getX() + 90, this.getY() + 25, this.getWidth() - 90D, this.getHeight() - 25, 3);
        }
        RRectUtils.drawRound(this.getX() + 90, this.getY(), this.getWidth() - 90, 20, 3, ColorUtils.getBackgroundColor(4));
        RRectUtils.drawRound(this.getX(), this.getY(), 85, this.getHeight(), 3, ColorUtils.getBackgroundColor(4));

        FontUtils.regular_bold26.drawString("LEGITISH", this.getX() + 9D, this.getY() + 6D, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(1).getRGB());

        RRectUtils.drawGradientRoundCorner(this.getX() + 5D, this.getY() + currentCategoryY, 75, 15, 4);

        for (Category category : categoryManager.getCategories()) {
            float addX = ! selectedCategory.equals(categoryManager.getCategoryByClass(SettingsCategory.class)) ? 88 : 95;

            if (!category.equals(categoryManager.getCategoryByClass(SettingsCategory.class))) {
                FontUtils.regular20.drawString(category.getName(), (int) this.getX() + 12, (int) this.getY() + categoryOffset + 5, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
            } else {
                RRectUtils.drawRound(this.getX(), this.getY() + this.getHeight() - 26, 85, 1, 0, ColorUtils.getBackgroundColor(2));
                FontUtils.icon20.drawString("I", (int) this.getX() + 12, (int) (this.getY() + this.getHeight()) - 14, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
                FontUtils.regular20.drawString(category.getName(), this.getX() + 26, (this.getY() + this.getHeight()) - 15, MinecraftFontRenderer.CenterMode.NONE, false, ColorUtils.getFontColor(2).getRGB());
            }

            StencilUtils.enableStencilBuffer();
            RRectUtils.drawRound((float) this.getX() + addX, (float) this.getY() + 25D, this.getWidth() - addX, this.getHeight() - 25D, 6D, ColorUtils.getBackgroundColor(1));
            StencilUtils.readStencilBuffer(1);

            if (category.equals(selectedCategory)) {
                if (!category.equals(categoryManager.getCategoryByClass(SettingsCategory.class))) {
                    currentCategoryY = categoryOffset;
                }
                category.drawScreen(mouseX, mouseY, partialTicks);
            }

            StencilUtils.disableStencilBuffer();

            categoryOffset += 25;
        }

        GlStateManager.popMatrix();
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int categoryOffset = 35;

        for (Category c : categoryManager.getCategories()) {
            if (!c.equals(categoryManager.getCategoryByClass(SettingsCategory.class))) {
                if (MouseUtils.mouseInBounds(mouseX, mouseY, this.getX() + 4, this.getY() + categoryOffset, 75, 16) && mouseButton == 0) {
                    selectedCategory = c;
                    openModSetting = false;
                }
            } else {
                if (MouseUtils.mouseInBounds(mouseX, mouseY, this.getX(), this.getY() + this.getHeight() - 30, 85, 30) && mouseButton == 0) {
                    selectedCategory = c;
                    openModSetting = true;
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
            if (openModSetting) {
                openModSetting = false;
            } else {
                mc.displayGuiScreen(null);
            }
        }
    }

    public void onGuiClosed() {
        openModSetting = false;
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

    public float getWidth() {
        return 305;
    }

    public float getHeight() {
        return 230;
    }
}
