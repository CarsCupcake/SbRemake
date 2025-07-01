package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemStackModifiers;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class EnchantedCharcoal implements ISbItem {
    @Override
    public String getId() {
        return "ENCHANTED_CHARCOAL";
    }

    @Override
    public String getName() {
        return "Enchanted Charcoal";
    }

    @Override
    public Material getMaterial() {
        return Material.CHARCOAL;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.builder().glint(true).build();
    }
}
