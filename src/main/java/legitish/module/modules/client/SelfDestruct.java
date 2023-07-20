package legitish.module.modules.client;

import legitish.main.Legitish;
import legitish.module.Module;

public class SelfDestruct extends Module {
    public static boolean destructed = false;

    public SelfDestruct() {
        super("Self Destruct", Module.category.Client, 0);
    }

    public void onEnable() {
        mc.displayGuiScreen(null);
        destructed = true;

        for (Module module : Legitish.moduleManager.moduleList()) {
            if (module != this) {
                module.disable();
            }
        }

        this.disable();
    }
}
