package me.carscupcake.sbremake.player.potion;

import com.google.gson.JsonObject;
import me.carscupcake.sbremake.config.ConfigConstructor;
import me.carscupcake.sbremake.config.ConfigParameter;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.config.DefaultConfigItem;

public record  PotionEffect(@ConfigParameter("id") IPotion potion,
                           @ConfigParameter("expiration") long expiration,
                           @ConfigParameter("amplifier") byte amplifier) implements DefaultConfigItem {
    @ConfigConstructor
    public PotionEffect {}

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
