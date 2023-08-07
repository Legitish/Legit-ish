package legitish.module.modules.minigames;

import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;

public class BedwarsAlerts extends Module {
    public static ModuleDesc desc;
    public static ModuleTickSetting sound, searchDetectives, announceMurderer;

    public BedwarsAlerts() {
        super("Murder Mystery", category.Visual, 0);
        this.registerSetting(desc = new ModuleDesc("Detects murderers and detectives in Murder Mystery."));
        this.registerSetting(sound = new ModuleTickSetting("Play sound", true));
        this.registerSetting(searchDetectives = new ModuleTickSetting("Search detectives", true));
        this.registerSetting(announceMurderer = new ModuleTickSetting("Announce murderer", false));
    }
}
