package legitish.module.modules.minigames;

import legitish.module.Module;
import legitish.module.modulesettings.ModuleTickSetting;

public class BedwarsAlerts extends Module {
    public static ModuleTickSetting fireball;

    public BedwarsAlerts() {
        super("Bedwars Alerts", category.Visual, 0);
        fireball = new ModuleTickSetting("Fireball", true);
    }
}
