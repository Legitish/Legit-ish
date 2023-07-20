package legitish.module.modulesettings;

import com.google.gson.JsonObject;
import legitish.module.ModuleSettingsList;

import java.util.ArrayList;


public class ModuleComboSetting extends ModuleSettingsList {
    private final String name;
    private final ArrayList<String> options;
    private String value;

    public ModuleComboSetting(String settingName, String value, ArrayList<String> options) {
        super(settingName);
        this.name = settingName;
        this.value = value;
        this.options = options;
    }

    public String get() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String s) {
        this.value = s;
    }

    public ArrayList<String> getOptions() {
        return this.options;
    }

    @Override
    public void resetToDefaults() {
        this.setValue(this.value);
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
        if (!data.get("type").getAsString().equals("combo")) {
            return;
        }
        setValue(data.get("value").getAsString());
    }

    @Override
    public JsonObject getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", "combo");
        data.addProperty("value", getValue());
        return data;
    }
}
