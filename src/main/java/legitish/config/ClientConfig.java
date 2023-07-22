package legitish.config;

import legitish.main.Legitish;
import legitish.module.modules.client.Arraylist;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientConfig {
    private final File clientConfigFile;
    private final String HUDX_prefix = "HUDX ~";
    private final String HUDY_prefix = "HUDY ~";
    private final String loadedConfigPrefix = "loaded-cfg~ ";

    public ClientConfig() {
        File clientConfigDirectory = new File(Minecraft.getMinecraft().mcDataDir, "config/legitish/client");
        if (!clientConfigDirectory.exists()) {
            if (!clientConfigDirectory.mkdirs()) {
                throw new RuntimeException("Error creating config directory!");
            }
        }

        clientConfigFile = new File(clientConfigDirectory, "config");
        if (!clientConfigFile.exists()) {
            try {
                if (clientConfigFile.createNewFile()) {
                    throw new RuntimeException("Error creating client config file!");
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }


    public void saveConfig() {
        List<String> clientConfig = new ArrayList<>();
        clientConfig.add(loadedConfigPrefix + Legitish.configManager.getConfig().getName());
        clientConfig.add(HUDX_prefix + Arraylist.getHudX());
        clientConfig.add(HUDY_prefix + Arraylist.getHudY());

        PrintWriter writer;
        try {
            writer = new PrintWriter(this.clientConfigFile);
            for (String line : clientConfig) {
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
            reader = new Scanner(this.clientConfigFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (reader != null) {
            while (reader.hasNextLine()) {
                configFileContents.add(reader.nextLine());
            }
        }

        return configFileContents;
    }
}
