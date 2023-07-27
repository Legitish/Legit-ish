package legitish.module.modulesettings;

import com.google.gson.JsonObject;

public class ModuleDesc extends ModuleSettingsList {
    private String desc;

    public ModuleDesc(String desc) {
        super(desc);
        this.desc = desc;
    }

    public String get() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
