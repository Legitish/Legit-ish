package legitish.module;

import com.google.gson.JsonObject;

import java.io.File;
import java.lang.reflect.Field;

public abstract class ModuleSettingsList {
    public String n;

    public ModuleSettingsList(String n) {
        this.n = n;
    }

    public String get() {
        return this.n;
    }

    public abstract void applyConfigFromJson(JsonObject data);

    public abstract JsonObject getConfigAsJson();
}
