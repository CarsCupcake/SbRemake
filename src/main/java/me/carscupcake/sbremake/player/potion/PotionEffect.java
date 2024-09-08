package me.carscupcake.sbremake.player.potion;

import com.google.gson.JsonObject;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.item.modifiers.potion.PotionInfo;

public record PotionEffect(IPotion potion, long expiration, byte amplifier) {
    public PotionEffect(String id, ConfigSection section) {
        this(IPotion.potions.get(id), section.get("expiration", ConfigSection.LONG) + System.currentTimeMillis(), section.get("amplifier", ConfigSection.BYTE));
    }
    public void store(ConfigSection f) {
        ConfigSection section = new ConfigSection(new JsonObject());
        section.set("expiration", expiration - System.currentTimeMillis(), ConfigSection.LONG);
        section.set("amplifier", amplifier, ConfigSection.BYTE);
        f.set(potion.getId(), section, ConfigSection.SECTION);
    }

    @Override
    public int hashCode() {
        return potion.hashCode();
    }
}
