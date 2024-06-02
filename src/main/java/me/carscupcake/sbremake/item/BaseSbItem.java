package me.carscupcake.sbremake.item;

import net.minestom.server.item.Material;

public record BaseSbItem(Material material, String name) implements ISbItem {

    @Override
    public String getId() {
        return material.namespace().value().toUpperCase();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }
}
