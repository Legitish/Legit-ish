package legitish.module.modulesettings.impl;

import com.google.gson.JsonObject;
import legitish.module.modulesettings.ModuleSettingsList;
import legitish.utils.MathUtils;

public class ModuleSliderSetting extends ModuleSettingsList {
    private final String name;
    private final double max;
    private final double min;
    private final double interval;
    private double val;

    public ModuleSliderSetting(String settingName, double defaultValue, double min, double max, double intervals) {
        super(settingName);
        this.name = settingName;
        this.val = defaultValue;
        this.min = min;
        this.max = max;
        this.interval = intervals;
    }

    public static double correct(double val, double min, double max) {
        val = Math.max(min, val);
        val = Math.min(max, val);
        return val;
    }

    public String get() {
        return this.name;
    }

    public double getInput() {
        return MathUtils.round(this.val, 2);
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }

    public void setValue(double n) {
        n = correct(n, this.min, this.max);
        n = (double) Math.round(n * (1.0D / this.interval)) / (1.0D / this.interval);
        this.val = n;
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
        if (!data.get("type").getAsString().equals("slider")) {
            return;
        }
        setValue(data.get("value").getAsDouble());
    }

    @Override
    public JsonObject getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", "slider");
        data.addProperty("value", getInput());
        return data;
    }
}
