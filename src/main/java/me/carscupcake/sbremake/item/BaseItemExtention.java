package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.item.Material;

import java.util.Map;

public record BaseItemExtention(Material base, String name, Map<Stat, Double> stats, ItemRarity rarity, ItemType type) implements ISbItem {
    public BaseItemExtention(Material base, Map<Stat, Double> stats, ItemRarity rarity, ItemType type) {
        this(base, StringUtils.connect(base.namespace().value().split("_"), true), stats, rarity, type);
    }
    @Override
    public String getId() {
        return base.namespace().value().toUpperCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return base;
    }

    @Override
    public ItemType getType() {
        return type;
    }

    @Override
    public ItemRarity getRarity() {
        return rarity;
    }
}
