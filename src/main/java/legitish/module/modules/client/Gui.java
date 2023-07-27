package legitish.module.modules.client;

import legitish.main.Legitish;
import legitish.module.Module;
import legitish.module.modulesettings.ModuleDesc;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.utils.GameUtils;

public class Gui extends Module {
    public static ModuleSliderSetting a;
    public static ModuleDesc b;

    public Gui() {
        super("Gui", category.Client, 38);
    }

    public void onEnable() {
        if (GameUtils.isPlayerInGame() && mc.currentScreen != Legitish.clickGui) {
            mc.displayGuiScreen(legitish.main.Legitish.clickGui);
            Legitish.clickGui.initMain();
        }

        this.disable();
    }
}
