package legitish.module.modulesettings.impl;

import legitish.module.modulesettings.ModuleSettingsList;

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
