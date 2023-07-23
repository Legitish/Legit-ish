package legitish.module.modulesettings;

import com.google.gson.JsonObject;
import legitish.module.ModuleSettingsList;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ModuleDoubleSliderSetting extends ModuleSettingsList {
    private final String name;
    private final double max;
    private final double min;
    private final double interval;
    private double valMax, valMin;

    public ModuleDoubleSliderSetting(String settingName, double defaultValueMin, double defaultValueMax, double min,
                                     double max, double intervals) {
        super(settingName);
        this.name = settingName;
        this.valMin = defaultValueMin;
        this.valMax = defaultValueMax;
        this.min = min;
        this.max = max;
        this.interval = intervals;
    }

    public static double correct(double val, double min, double max) {
        val = Math.max(min, val);
        val = Math.min(max, val);
        return val;
    }

    public static double round(double val, int p) {
        if (p < 0) {
            return 0.0D;
        } else {
            BigDecimal bd = new BigDecimal(val);
            bd = bd.setScale(p, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }

    public String get() {
        return this.name;
    }

    public double getInputMin() {
        return round(this.valMin, 2);
    }

    public double getInputMax() {
        return round(this.valMax, 2);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public void setValueMin(double n) {
        n = correct(n, this.min, this.valMax);
        n = (double) Math.round(n * (1.0D / this.interval)) / (1.0D / this.interval);
        this.valMin = n;
    }

    public void setValueMax(double n) {
        n = correct(n, this.valMin, this.max);
        n = (double) Math.round(n * (1.0D / this.interval)) / (1.0D / this.interval);
        this.valMax = n;
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
        if (!data.get("type").getAsString().equals("doubleSlider")) {
            return;
        }
        setValueMin(data.get("valueMin").getAsDouble());
        setValueMax(data.get("valueMax").getAsDouble());
    }

    @Override
    public JsonObject getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", "doubleSlider");
        data.addProperty("valueMin", getInputMin());
        data.addProperty("valueMax", getInputMax());
        return data;
    }
}
