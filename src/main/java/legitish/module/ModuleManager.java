package legitish.module;

import legitish.module.modules.client.Arraylist;
import legitish.module.modules.client.Gui;
import legitish.module.modules.combat.*;
import legitish.module.modules.movement.KeepSprint;
import legitish.module.modules.movement.Sprint;
import legitish.module.modules.player.*;
import legitish.module.modules.render.*;
import legitish.utils.MouseUtils;
import legitish.utils.font.MinecraftFontRenderer;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static Module fastPlace;
    public static Module antiBot;
    public static Module autoClicker;
    public static Module reach;
    public static Module arrayList;
    public static Module playerESP;
    public static Module gui;
    static List<Module> moduleList = new ArrayList<>();
    static List<Module> enabledModuleList = new ArrayList<>();

    public static void sort() {
        enabledModuleList.sort((o1, o2) -> MouseUtils.mc.fontRendererObj.getStringWidth(o2.getName()) - MouseUtils.mc.fontRendererObj.getStringWidth(o1.getName()));
    }

    public void RegisterMods() {
        // Combat
        this.addModule(autoClicker = new AutoClicker());
        this.addModule(new AimAssist());
        this.addModule(new ClickAssist());
        this.addModule(new DelayRemover());
        this.addModule(new Dragclicker());
        this.addModule(reach = new Reach());
        this.addModule(new RodAimbot());
        this.addModule(new Velocity());
        // Movement
        this.addModule(new KeepSprint());
        this.addModule(new Sprint());
        // Player
        this.addModule(new AutoJump());
        this.addModule(new AutoPlace());
        this.addModule(fastPlace = new FastPlace());
        this.addModule(new Freecam());
        this.addModule(new SafeWalk());
        // Visual
        this.addModule(antiBot = new AntiBot());
        this.addModule(arrayList = new Arraylist());
        this.addModule(new BedPlates());
        this.addModule(new Chams());
        this.addModule(new ChestESP());
        this.addModule(new MurderMystery());
        this.addModule(new Nametags());
        this.addModule(playerESP = new PlayerESP());
        this.addModule(new Tracers());
        this.addModule(new Xray());
        // Client
        this.addModule(gui = new Gui());
        this.enableDefaultModules();
    }

    public void enableDefaultModules() {
        arrayList.enable();
        antiBot.enable();
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
