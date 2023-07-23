package legitish.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {
    public static MinecraftFontRenderer
            regular16,
            regular20,
            regular_bold26,
            regular_bold30,
            icon20;
    public static Font
            regular16_,
            regular20_,
            regular_bold26_, regular_bold30_,
            icon20_;
    private static int prevScale;

    public static void init() {
        Map<String, Font> locationMap = new HashMap<>();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int scale = sr.getScaleFactor();

        if (scale != prevScale) {
            prevScale = scale;

            FontUtils.regular16_ = FontUtils.getFont(locationMap, "regular.ttf", 16);
            FontUtils.regular16 = new MinecraftFontRenderer(FontUtils.regular16_);
            FontUtils.regular20_ = FontUtils.getFont(locationMap, "regular.ttf", 20);
            FontUtils.regular20 = new MinecraftFontRenderer(FontUtils.regular20_);

            FontUtils.regular_bold26_ = FontUtils.getFont(locationMap, "regular_bold.ttf", 26);
            FontUtils.regular_bold26 = new MinecraftFontRenderer(FontUtils.regular_bold26_);
            FontUtils.regular_bold30_ = FontUtils.getFont(locationMap, "regular_bold.ttf", 30);
            FontUtils.regular_bold30 = new MinecraftFontRenderer(FontUtils.regular_bold26_);
            
            FontUtils.icon20_ = FontUtils.getFont(locationMap, "icon.ttf", 20);
            FontUtils.icon20 = new MinecraftFontRenderer(FontUtils.icon20_);
        }
    }

    public static Font getFont(Map<String, Font> locationMap, String location, double size) {
        Font font;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        size = size * ((double) sr.getScaleFactor() / 2);

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, (float) size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("legitish/fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(Font.PLAIN, (float) size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            font = new Font("default", Font.PLAIN, +10);
        }
        return font;
    }
}
