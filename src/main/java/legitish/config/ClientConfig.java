package legitish.config;

import legitish.main.Legitish;
import legitish.module.modules.render.Arraylist;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientConfig {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private final File configFile;
    private final File configDir;
    private final String HUDX_prefix = "HUDX ~";
    private final String HUDY_prefix = "HUDY ~";
    private final String fileName = "config";
    private final String loadedConfigPrefix = "loaded-cfg~ ";

    public ClientConfig() {
        configDir = new File(Minecraft.getMinecraft().mcDataDir, "config/legitish/client");
        if (!configDir.exists()) {
            configDir.mkdir();
        }

        configFile = new File(configDir, fileName);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void saveConfig() {
        List<String> config = new ArrayList<>();
        config.add(loadedConfigPrefix + Legitish.configManager.getConfig().getName());
        config.add(HUDX_prefix + Arraylist.getHudX());
        config.add(HUDY_prefix + Arraylist.getHudY());

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(this.configFile);
            for (String line : config) {
                writer.println(line);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyConfig() {
        List<String> config = this.parseConfigFile();

        for (String line : config) {
            if (line.startsWith(HUDX_prefix)) {
                try {
                    Arraylist.setHudX(Integer.parseInt(line.replace(HUDX_prefix, "")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (line.startsWith(HUDY_prefix)) {
                try {
                    Arraylist.setHudY(Integer.parseInt(line.replace(HUDY_prefix, "")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (line.startsWith(loadedConfigPrefix)) {
                Legitish.configManager.loadConfigByName(line.replace(loadedConfigPrefix, ""));
            }
        }
    }

    private List<String> parseConfigFile() {
        List<String> configFileContents = new ArrayList<>();
        Scanner reader = null;
        try {
            reader = new Scanner(this.configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (reader.hasNextLine())
            configFileContents.add(reader.nextLine());

        return configFileContents;
    }
}
