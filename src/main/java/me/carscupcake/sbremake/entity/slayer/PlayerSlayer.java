package me.carscupcake.sbremake.entity.slayer;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;

@Getter
public class PlayerSlayer {
    private final SkyblockPlayer player;
    private final ISlayer slayer;
    private int level = 0;
    private int xp;

    public PlayerSlayer(SkyblockPlayer player, ISlayer slayer) {
        this.player = player;
        this.slayer = slayer;
        ConfigSection file = new ConfigFile("slayer", player).get(slayer.getId(), ConfigSection.SECTION, new ConfigSection(new JsonObject()));
        this.xp = file.get("xp", ConfigSection.INTEGER, 0);
        for (int i = 0; i < slayer.getMaxLevel(); i++) {
            if (slayer.requiredXp(i) <= xp) level++;
        }
    }

    public void addXp(int xp) {
        this.xp += xp;
        while (level < slayer.getMaxLevel() && this.xp >= slayer.requiredXp(level)) {
            level++;
            //TODO level up message
        }
    }

    public void save(ConfigFile f) {
        ConfigSection section = new ConfigSection(new JsonObject());
        section.set("xp", xp, ConfigSection.INTEGER);
        f.set(slayer.getId(), section, ConfigSection.SECTION);
    }


}
