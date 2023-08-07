package legitish.module.modulesettings.impl;

import com.google.gson.JsonObject;
import legitish.module.modulesettings.ModuleSettingsList;

public class ModuleTickSetting extends ModuleSettingsList {
    private final String name;
    private boolean isEnabled;

    public ModuleTickSetting(String name, boolean isEnabled) {
        super(name);
        this.name = name;
        this.isEnabled = isEnabled;
    }

    @Override
    public String get() {
        return this.name;
    }

    public boolean isToggled() {
        return this.isEnabled;
    }

    public void setEnabled(boolean b) {
        this.isEnabled = b;
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
        if (!data.get("type").getAsString().equals("tick")) {
            return;
        }
        setEnabled(data.get("value").getAsBoolean());
    }

    @Override
    public JsonObject getConfigAsJson() {
        JsonObject data = new JsonObject();
        data.addProperty("type", "tick");
        data.addProperty("value", isToggled());
        return data;
    }
}
