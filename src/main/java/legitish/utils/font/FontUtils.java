package legitish.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {
    public static MinecraftFontRenderer regular16, regular20, regular_bold26, regular_bold30, icon20;
    private static int prevScale;

    public static void init() {
        Map<String, Font> locationMap = new HashMap<>();

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        int scale = sr.getScaleFactor();

        if (scale != prevScale) {
            prevScale = scale;

            FontUtils.regular16 = new MinecraftFontRenderer(FontUtils.getFont(locationMap, "regular.ttf", 16));
            FontUtils.regular20 = new MinecraftFontRenderer(FontUtils.getFont(locationMap, "regular.ttf", 20));

            FontUtils.regular_bold26 = new MinecraftFontRenderer(FontUtils.getFont(locationMap, "regular_bold.ttf", 26));
            FontUtils.regular_bold30 = new MinecraftFontRenderer(FontUtils.getFont(locationMap, "regular_bold.ttf", 30));

            FontUtils.icon20 = new MinecraftFontRenderer(FontUtils.getFont(locationMap, "icon.ttf", 20));
        }
    }

    public static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font;

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        size = (int) (size * ((double) sr.getScaleFactor() / 2));

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("legitish/fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }
}
