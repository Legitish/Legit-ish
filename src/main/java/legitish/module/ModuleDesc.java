package legitish.module;

import com.google.gson.JsonObject;

public class ModuleDesc extends ModuleSettingsList {
    private String desc;

    public ModuleDesc(String t) {
        super(t);
        this.desc = t;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String t) {
        this.desc = t;
    }

    @Override
    public void applyConfigFromJson(JsonObject data) {
    }

    @Override
    public JsonObject getConfigAsJson() {
        return null;
    }

    @Override
    public void resetToDefaults() {
    }
}
