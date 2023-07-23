package legitish.module.modules.client;

import legitish.main.Legitish;
import legitish.module.Module;
import legitish.module.ModuleDesc;
import legitish.module.modulesettings.ModuleSliderSetting;
import legitish.utils.GameUtils;

public class Gui extends Module {
    public static ModuleSliderSetting a;
    public static ModuleDesc b;

    public Gui() {
        super("Gui", Module.category.Client, 38);
        this.registerSetting(a = new ModuleSliderSetting("Theme", 3.0D, 1.0D, 3.0D, 1.0D));
        this.registerSetting(b = new ModuleDesc("Mode: 3"));
    }

    public void onEnable() {
        if (GameUtils.isPlayerInGame() && mc.currentScreen != Legitish.clickGui) {
            mc.displayGuiScreen(legitish.main.Legitish.clickGui);
            Legitish.clickGui.initMain();
        }

        this.disable();
    }

    public void guiUpdate() {
        switch ((int) a.getInput()) {
            case 1:
                b.setDesc("Mode: 1");
                break;
            case 2:
                b.setDesc("Mode: 2");
                break;
            case 3:
                b.setDesc("Mode: 3");
        }
    }
}
