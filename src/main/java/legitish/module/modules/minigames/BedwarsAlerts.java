package legitish.module.modules.minigames;

import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;
import legitish.module.modulesettings.impl.ModuleTickSetting;

public class BedwarsAlerts extends Module {
    public static ModuleTickSetting sound, searchDetectives, announceMurderer;

    public BedwarsAlerts() {
        super("Bedwars Alerts", category.Visual, 0);
        this.registerSetting(new ModuleDesc("Alerts of certain events in Bedwars."));
        this.registerSetting(sound = new ModuleTickSetting("Play sound", true));
        this.registerSetting(searchDetectives = new ModuleTickSetting("Search detectives", true));
        this.registerSetting(announceMurderer = new ModuleTickSetting("Announce murderer", false));
    }
}
