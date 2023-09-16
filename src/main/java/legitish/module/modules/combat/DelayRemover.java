package legitish.module.modules.combat;

import legitish.module.Module;
import legitish.module.modulesettings.impl.ModuleDesc;

public class DelayRemover extends Module {
    public DelayRemover() {
        super("Delay Remover", category.Combat, 0);
        this.registerSetting(new ModuleDesc("Removes invis delay between hits."));
    }
}
