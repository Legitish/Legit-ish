package legitish.module.modulesettings;

import com.google.gson.JsonObject;

public class ModuleSettingsList {
    public final String n;

    public ModuleSettingsList(String n) {
        this.n = n;
    }

    public String get() {
        return this.n;
    }

    public void applyConfigFromJson(JsonObject data) {

    }

    public JsonObject getConfigAsJson() {
        return null;
    }
}
