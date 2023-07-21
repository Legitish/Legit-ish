package legitish.module;

import com.google.gson.JsonObject;
import legitish.main.Legitish;
import legitish.module.modulesettings.ModuleTickSetting;
import legitish.utils.AnimationUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Iterator;

public class Module {
    protected static Minecraft mc;
    private final String moduleName;
    private final Module.category moduleCategory;
    public AnimationUtils buttonAnimation = new AnimationUtils(0.0F);
    protected ArrayList<ModuleSettingsList> settings;
    private boolean isToggled = false;
    private boolean enabled;
    private int keycode;
    protected int defaultKeyCode = keycode;

    public Module(String moduleName, Module.category moduleCategory, int keycode) {
        this.moduleName = moduleName;
        this.moduleCategory = moduleCategory;
        this.keycode = keycode;
        this.enabled = false;
        mc = Minecraft.getMinecraft();
        this.settings = new ArrayList<>();
    }

    public Module(String name, Module.category moduleCategory) {
        this.moduleName = name;
        this.moduleCategory = moduleCategory;
        this.keycode = 0;
        this.enabled = false;
    }

    public static Module getModule(Class<? extends Module> a) {
        Iterator<Module> var1 = ModuleManager.moduleList.iterator();

        Module module;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            module = var1.next();
        } while (module.getClass() != a);

        return module;
    }

    public void keybind() {
        if (this.keycode != 0) {
            if (!this.isToggled && Keyboard.isKeyDown(this.keycode)) {
                this.toggle();
                this.isToggled = true;
            } else if (!Keyboard.isKeyDown(this.keycode)) {
                this.isToggled = false;
            }

        }
    }

    public void enable() {
        this.setEnabled(true);
        ModuleManager.enabledModuleList.add(this);
        if (ModuleManager.arrayList.isEnabled()) {
            ModuleManager.sort();
        }
        net.weavemc.loader.api.event.EventBus.subscribe(this);
        Legitish.eventBus.register(this);
        this.onEnable();
    }

    public void disable() {
        this.setEnabled(false);
        ModuleManager.enabledModuleList.remove(this);
        Legitish.eventBus.unregister(this);
        net.weavemc.loader.api.event.EventBus.unsubscribe(this);
        this.onDisable();
    }

    public String getName() {
        return this.moduleName;
    }

    public ArrayList<ModuleSettingsList> getSettings() {
        return this.settings;
    }

    public void registerSetting(ModuleSettingsList Setting) {
        this.settings.add(Setting);
    }

    public Module.category moduleCategory() {
        return this.moduleCategory;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void toggle() {
        if (this.isEnabled()) {
            this.disable();
        } else {
            this.enable();
        }

    }

    public void update() {
    }

    public void guiUpdate() {
    }

    public void guiButtonToggled(ModuleTickSetting b) {
    }

    public int getKeycode() {
        return this.keycode;
    }

    public void setbind(int keybind) {
        this.keycode = keybind;
    }

    public JsonObject getConfigAsJson() {
        JsonObject settings = new JsonObject();

        for (ModuleSettingsList setting : this.settings) {
            if (!(setting instanceof ModuleDesc)) {
                JsonObject settingData = setting.getConfigAsJson();
                settings.add(setting.n, settingData);
            }
        }

        JsonObject data = new JsonObject();
        data.addProperty("keycode", keycode);
        data.add("settings", settings);

        return data;
    }

    public void applyConfigFromJson(JsonObject data) {
        try {
            this.keycode = data.get("keycode").getAsInt();
            JsonObject settingsData = data.get("settings").getAsJsonObject();
            for (ModuleSettingsList setting : this.settings) {
                if (settingsData.has(setting.get())) {
                    setting.applyConfigFromJson(settingsData.get(setting.get()).getAsJsonObject());
                }
            }
        } catch (NullPointerException ignored) {

        }
    }

    public void resetToDefaults() {
        this.keycode = defaultKeyCode;
        Legitish.moduleManager.enableDefaultModules();

        for (ModuleSettingsList setting : this.settings) {
            setting.resetToDefaults();
        }
    }

    public enum category {
        Combat,
        Minigames,
        Movement,
        Player,
        Visual,
        Client
    }
}
