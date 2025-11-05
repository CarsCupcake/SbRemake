package me.carscupcake.sbremake.player.potion;

import me.carscupcake.sbremake.config.ConfigConstructor;
import me.carscupcake.sbremake.config.ConfigParameter;
import me.carscupcake.sbremake.config.DefaultConfigItem;

public record  PotionEffect(@ConfigParameter("id") IPotion potion,
                           @ConfigParameter("expiration") long expiration,
                           @ConfigParameter("amplifier") byte amplifier) implements DefaultConfigItem {
    @ConfigConstructor
    public PotionEffect {}

    @Override
    public int hashCode() {
        return potion.hashCode();
    }
}
