package me.carscupcake.sbremake.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.UUID;

@Getter
public class ConfigFile extends ConfigSection {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private final File file;
    public static final File DATA_PATH = new File(Paths.get("").toAbsolutePath().toString(), "data");

    static {
        DATA_PATH.mkdirs();
    }

    public ConfigFile(String name, SkyblockPlayer player) {
        this(new File(getConfigFolder(player), String.format("%s.json", name)));
    }

    public ConfigFile(String name, UUID configId) {
        this(new File(getConfigFolder(configId), String.format("%s.json", name)));
    }

    public ConfigFile(String name) {
        this(new File(DATA_PATH, String.format("%s.json", name)));
    }

    public ConfigFile(File file) {
        super(read(file));
        this.file = file.getAbsoluteFile();
    }

    private static JsonElement read(File file) {
        if (!file.exists()) return null;
        try {
            return gson.fromJson(new FileReader(file), JsonElement.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static File getConfigFolder(SkyblockPlayer player) {
        return getConfigFolder(player.getConfigId());
    }

    public static File getConfigFolder(UUID playerID) {
        return new File(DATA_PATH, playerID.toString());
    }

    public void save() {
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file);
            gson.toJson((element == null) ? new JsonObject() : element, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
