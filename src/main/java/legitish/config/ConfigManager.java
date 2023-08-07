package legitish.config;

import com.google.gson.JsonObject;
import legitish.main.Legitish;
import legitish.module.Module;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConfigManager {
    private final File configDirectory = new File(Minecraft.getMinecraft().mcDataDir + File.separator + "config" + File.separator + "legitish");
    private final ArrayList<Config> configs = new ArrayList<>();
    private Config config;

    public ConfigManager() {
        if (!configDirectory.isDirectory()) {
            if (!configDirectory.mkdirs()) {
                throw new RuntimeException("Error creating config directory!");
            }
        }

        discoverConfigs();
        File defaultFile = new File(configDirectory, "default.lcfg");
        this.config = new Config(defaultFile);

        if (!defaultFile.exists()) {
            saveConfig();
        }
    }

    public void discoverConfigs() {
        configs.clear();
        if (configDirectory.listFiles() == null || !(Objects.requireNonNull(configDirectory.listFiles()).length > 0)) {
            return;
        }

        for (File file : Objects.requireNonNull(configDirectory.listFiles())) {
            if (file.getName().endsWith(".lcfg")) {
                configs.add(new Config(
                        new File(file.getPath())
                ));
            }
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
        JsonObject data = config.getData();
        if (data != null) {
            data = data.get("modules").getAsJsonObject();
            List<Module> knownModules = new ArrayList<>(Legitish.moduleManager.moduleList());
            for (Module module : knownModules) {
                if (data.has(module.getName())) {
                    module.applyConfigFromJson(data.get(module.getName()).getAsJsonObject());
                }
            }
        } else {
            saveConfig();
        }
    }

    public void saveConfig() {
        JsonObject data = new JsonObject();
        data.addProperty("title", "");
        data.addProperty("author", "Unknown");
        data.addProperty("notes", "");
        data.addProperty("lastEditTime", System.currentTimeMillis());

        JsonObject modules = new JsonObject();
        for (Module module : Legitish.moduleManager.moduleList()) {
            modules.add(module.getName(), module.getConfigAsJson());
        }
        data.add("modules", modules);

        config.saveConfigData(data);
    }

    public void loadConfigByName(String replace) {
        discoverConfigs();
        for (Config config : configs) {
            if (config.getName().equals(replace)) {
                setConfig(config);
            }
        }
    }
}
