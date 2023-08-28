package legitish.module;

import legitish.module.modules.client.Arraylist;
import legitish.module.modules.client.Gui;
import legitish.module.modules.client.Notifications;
import legitish.module.modules.client.Targets;
import legitish.module.modules.combat.*;
import legitish.module.modules.minigames.BedPlates;
import legitish.module.modules.minigames.BedwarsAlerts;
import legitish.module.modules.minigames.MurderMystery;
import legitish.module.modules.movement.KeepSprint;
import legitish.module.modules.movement.NoSlow;
import legitish.module.modules.movement.Sprint;
import legitish.module.modules.player.*;
import legitish.module.modules.render.*;
import legitish.utils.font.FontUtils;
import legitish.utils.font.MinecraftFontRenderer;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static final List<Module> moduleList = new ArrayList<>();
    public static final List<Module> enabledModuleList = new ArrayList<>();
    public static Module fastPlace, targets, autoClicker, reach, arrayList, playerESP, gui, notifications, noSlow;

    public static void sort() {
        enabledModuleList.sort((o1, o2) -> (int) (FontUtils.regular16.getStringWidth(o2.getName()) - FontUtils.regular16.getStringWidth(o1.getName())));
    }

    public void RegisterMods() {
        // Combat
        this.addModule(autoClicker = new AutoClicker());
        this.addModule(new AimAssist());
        //this.addModule(new AutoBlock());
        this.addModule(new ClickAssist());
        this.addModule(new DelayRemover());
        this.addModule(reach = new Reach());
        this.addModule(new SprintReset());
        this.addModule(new Velocity());
        // Minigames
        this.addModule(new BedwarsAlerts());
        this.addModule(new BedPlates());
        this.addModule(new MurderMystery());
        // Movement
        this.addModule(new KeepSprint());
        this.addModule(noSlow = new NoSlow());
        this.addModule(new Sprint());
        // Player
        this.addModule(new AutoJump());
        this.addModule(new AutoPlace());
        this.addModule(fastPlace = new FastPlace());
        this.addModule(new Freecam());
        this.addModule(new Refill());
        this.addModule(new SafeWalk());
        // Visual
        this.addModule(new Chams());
        this.addModule(new ChestESP());
        this.addModule(new Nametags());
        this.addModule(playerESP = new PlayerESP());
        this.addModule(new Tracers());
        this.addModule(new Xray());
        // Client
        this.addModule(arrayList = new Arraylist());
        this.addModule(gui = new Gui());
        this.addModule(notifications = new Notifications());
        this.addModule(targets = new Targets());
        this.enableDefaultModules();
    }

    public void enableDefaultModules() {
        targets.enable();
        notifications.enable();
        arrayList.enable();
    }

    private void addModule(Module m) {
        moduleList.add(m);
    }

    public List<Module> moduleList() {
        return moduleList;
    }

    public List<Module> enabledModuleList() {
        return enabledModuleList;
    }

    public List<Module> inCategory(Module.category category) {
        ArrayList<Module> categoryML = new ArrayList<>();

        for (Module mod : this.moduleList()) {
            if (mod.moduleCategory().equals(category)) {
                categoryML.add(mod);
            }
        }

        return categoryML;
    }

    public int getLongestActiveModule(MinecraftFontRenderer mfr) {
        int length = 0;
        for (Module mod : enabledModuleList) {
            if (mod.isEnabled()) {
                if (mfr.getStringWidth(mod.getName()) > length) {
                    length = (int) mfr.getStringWidth(mod.getName());
                }
            }
        }
        return length;
    }
}
