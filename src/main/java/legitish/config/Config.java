package legitish.config;

import com.google.gson.*;

import java.io.*;
import java.util.Objects;

public class Config {
    public final File file;
    public long creationDate;

    public Config(File pathToFile) {
        this.file = pathToFile;

        if (!file.exists()) {
            creationDate = System.currentTimeMillis();
            try {
                if (!file.createNewFile()) {
                    throw new RuntimeException("Error creating config file!");
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        } else {
            try {
                creationDate = getData().get("creationTime").getAsLong();
            } catch (NullPointerException e) {
                creationDate = 0L;
            }
        }
    }

    public String getName() {
        return file.getName().replace(".lcfg", "");
    }

    public JsonObject getData() {
        JsonParser jsonParser = new JsonParser();
        try (FileReader reader = new FileReader(file)) {
            JsonElement obj = jsonParser.parse(reader);
            return (JsonObject) obj;
        } catch (JsonSyntaxException | ClassCastException | IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public void saveConfigData(JsonObject data) {
        data.addProperty("creationTime", creationDate);
        try (PrintWriter config = new PrintWriter(new FileWriter(file))) {
            config.write(data.toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
