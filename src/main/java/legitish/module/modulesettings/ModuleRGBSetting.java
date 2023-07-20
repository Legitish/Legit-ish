package legitish.module.modulesettings;

import com.google.gson.JsonObject;
import legitish.module.ModuleSettingsList;

import java.awt.*;

public class ModuleRGBSetting extends ModuleSettingsList {
    private final String name;
    private final int[] defaultColour;
    private int[] colour;
    private int colorRGB;

    public ModuleRGBSetting(String name, int defaultRed, int defaultGreen, int defaultBlue) {
        super(name);
        this.name = name;
        this.defaultColour = new int[]{defaultRed, defaultGreen, defaultBlue};
        this.colour = new int[]{defaultRed, defaultGreen, defaultBlue};
        colorRGB = new Color(defaultRed, defaultGreen, defaultBlue).getRGB();
    }

    public String getSettingType() {
        return "rgbsetting";
    }

    public int getRed() {
        return this.colour[0];
    }

    public void setRed(int red) {
        setColor(0, red);
    }

    public int getGreen() {
        return this.colour[1];
    }

    public void setGreen(int green) {
        setColor(1, green);
    }

    public int getBlue() {
        return this.colour[2];
    }

    public void setBlue(int blue) {
        setColor(2, blue);
    }

    public int[] getColors() {
        return this.colour;
    }

    public void setColors(int[] colour) {
        this.colour = colour.clone();
    }

    public int getColor(int colour) {
        return this.colour[colour];
    }

    public int getRGB() {
        return colorRGB;
    }

    public void setColor(int colour, int value) {
        value = value > 255 ? 255 : value < 0 ? 0 : value;
        this.colour[colour] = value;
        this.colorRGB = new Color(this.colour[0], this.colour[1], this.colour[2]).getRGB();
    }

    @Override
    public void resetToDefaults() {
        //this.setValueMin(defaultVal);
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
        if (!data.get("type").getAsString().equals(getSettingType())) {
        }

        //setValue(data.get("value").getAsDouble());
    }

    @Override
    public JsonObject getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", getSettingType());
        data.addProperty("value", get());
        return data;
    }
}
