package legitish.utils.font;

import legitish.utils.ColorUtils;
import legitish.utils.render.GLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MinecraftFontRenderer extends CharRenderer {
    // Credit to EldoDebug for this one :clown:
    CharData[] boldChars = new CharData[256],
            italicChars = new CharData[256],
            boldItalicChars = new CharData[256];
    int[] colorCode = new int[32];
    DynamicTexture texBold, texItalic, texItalicBold;
    String colorcodeIdentifiers = "0123456789abcdefklmnor";

    public MinecraftFontRenderer(Font font) {
        super(font, true, true);
        this.setupMinecraftColorcodes();
        this.setupBoldItalicIDs();
    }

    public int drawString(String text, double x, double y, CenterMode centerMode, boolean shadow, int color) {
        switch (centerMode) {
            case X:
                if (shadow) {
                    this.drawString(text, x - this.getStringWidth(text) / 2 + 0.5, y + 0.5, color, true);
                }
                return (int) this.drawString(text, x - this.getStringWidth(text) / 2, y, color, false);
            case Y:
                if (shadow) {
                    this.drawString(text, x + 0.5, y - this.getHeight() / 2 + 0.5, color, true);
                }
                return (int) this.drawString(text, x, y - this.getHeight() / 2, color, false);
            case XY:
                if (shadow) {
                    this.drawString(text, x - this.getStringWidth(text) / 2 + 0.5, y - this.getHeight() / 2 + 0.5, color, true);
                }
                return (int) this.drawString(text, x - this.getStringWidth(text) / 2, y - this.getHeight() / 2, color, false);
            case NONE:
                if (shadow) {
                    this.drawString(text, x + 0.5, y + 0.5, color, true);
                }
                return (int) this.drawString(text, x, y, color, false);
        }
        return 0;
    }

    private double drawString(String text, double x, double y, int color, boolean shadow) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (text == null) {
            return 0;
        }

        if (shadow) {
            color = (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
        }

        FontUtils.init();

        CharData[] currentData = this.charData;
        double alpha = (color >> 24 & 255) / 255f;
        x = (x - 1) * sr.getScaleFactor();
        y = (y - 3) * sr.getScaleFactor() - 0.2;
        GL11.glPushMatrix();
        GL11.glScaled((double) 1 / sr.getScaleFactor(), 1 / (double) sr.getScaleFactor(), 1 / (double) sr.getScaleFactor());
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        ColorUtils.setColor(color);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(this.tex.getGlTextureId());
        GLUtils.bindTexture(this.tex.getGlTextureId());

        GlStateManager.enableBlend();

        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);

            if (character == '\u00a7') {
                int colorIndex = 21;

                try {
                    colorIndex = colorcodeIdentifiers.indexOf(text.charAt(index + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (colorIndex < 16) {
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;

                    if (colorIndex < 0) {
                        colorIndex = 15;
                    }

                    if (shadow) {
                        colorIndex += 16;
                    }

                    ColorUtils.setColor(this.colorCode[colorIndex], alpha);
                } else {
                    ColorUtils.setColor(color);
                    GlStateManager.bindTexture(this.tex.getGlTextureId());
                    currentData = this.charData;
                }

                ++index;
            } else if (character < currentData.length) {
                drawLetter(x, y, currentData, character);

                x += currentData[character].width - 8.3 + this.charOffset;
            }
        }
        GlStateManager.disableBlend();
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glPopMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        return x / 2f;
    }

    private void drawLetter(double x, double y, CharData[] currentData, char character) {
        GL11.glBegin(4);
        this.drawChar(currentData, character, x, y);
        GL11.glEnd();
    }

    public double getStringWidth(String text) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        if (text == null) {
            return 0;
        }

        double width = 0;
        CharData[] currentData = charData;

        for (int index = 0; index < text.length(); index++) {
            char character = text.charAt(index);

            if (character == '\u00a7') {
                ++index;
            } else if (character < currentData.length) {
                width += currentData[character].width - 8.3f + charOffset;
            }
        }

        return width / (double) sr.getScaleFactor();
    }

    public double getHeight() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        return (this.fontHeight - 8) / (double) sr.getScaleFactor();
    }

    @Override
    public void setFont(Font font) {
        super.setFont(font);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setAntiAlias(boolean antiAlias) {
        super.setAntiAlias(antiAlias);
        this.setupBoldItalicIDs();
    }

    @Override
    public void setFractionalMetrics(boolean fractionalMetrics) {
        super.setFractionalMetrics(fractionalMetrics);
        this.setupBoldItalicIDs();
    }

    private void setupBoldItalicIDs() {
        this.texBold = this.setupTexture(this.font.deriveFont(Font.BOLD), this.antiAlias, this.fractionalMetrics, this.boldChars);
        this.texItalic = this.setupTexture(this.font.deriveFont(Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.italicChars);
        this.texItalicBold = this.setupTexture(this.font.deriveFont(Font.BOLD | Font.ITALIC), this.antiAlias, this.fractionalMetrics, this.boldItalicChars);
    }

    public void wrapText(String text, double x, double y, CenterMode centerMode, boolean shadow, int color, double width, double heightIncrement) {
        List<String> lines = new ArrayList<>();
        String[] words = text.trim().split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            double totalWidth = getStringWidth(line + " " + word);

            if (x + totalWidth >= x + width) {
                lines.add(line.toString());
                line = new StringBuilder(word).append(" ");
                continue;
            }

            line.append(word).append(" ");
        }
        lines.add(line.toString());

        double newY = y - (centerMode == CenterMode.XY || centerMode == CenterMode.Y ? ((lines.size() - 1) * (getHeight() + heightIncrement)) / 2 : 0);
        // add x centermode support never !!!!
        for (String s : lines) {
            ColorUtils.resetColor();
            drawString(s, x, newY, centerMode, shadow, color);
            newY += getHeight() + heightIncrement;
        }
    }

    private void setupMinecraftColorcodes() {
        int index = 0;

        while (index < 32) {
            int noClue = (index >> 3 & 1) * 85;
            int red = (index >> 2 & 1) * 170 + noClue;
            int green = (index >> 1 & 1) * 170 + noClue;
            int blue = (index & 1) * 170 + noClue;

            if (index == 6) {
                red += 85;
            }

            if (index >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }

            this.colorCode[index] = (red & 255) << 16 | (green & 255) << 8 | blue & 255;
            ++index;
        }
    }

    public enum CenterMode {
        X,
        Y,
        XY,
        NONE
    }
}
