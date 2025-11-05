package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.config.Constants;
import me.carscupcake.sbremake.config.ConfigConstructor;
import me.carscupcake.sbremake.config.ConfigField;
import me.carscupcake.sbremake.config.ConfigParameter;
import me.carscupcake.sbremake.config.DefaultConfigItem;
import me.carscupcake.sbremake.item.ItemRarity;

public record AttributeEntry(@ConfigField IAttributeShard attributeShard, @ConfigField int amount, int level) implements DefaultConfigItem {
    @ConfigConstructor
    public AttributeEntry(@ConfigParameter("attributeShard") IAttributeShard attributeShard, @ConfigParameter("amount") int amount) {
        this(attributeShard, amount, toLevel(attributeShard.getRarity(), amount));
    }

    private static int toLevel(ItemRarity rarity, int amount) {
        var stacks = switch (rarity) {
            case COMMON -> Constants.ATTRIBUTE_LEVELLING.COMMON;
            case UNCOMMON -> Constants.ATTRIBUTE_LEVELLING.UNCOMMON;
            case RARE -> Constants.ATTRIBUTE_LEVELLING.RARE;
            case EPIC -> Constants.ATTRIBUTE_LEVELLING.EPIC;
            default -> Constants.ATTRIBUTE_LEVELLING.LEGENDARY;
        };
        int level = 0;
        while (amount >= stacks[level]) {
            amount -= stacks[level];
            level++;
        }
        return level;
    }
}
