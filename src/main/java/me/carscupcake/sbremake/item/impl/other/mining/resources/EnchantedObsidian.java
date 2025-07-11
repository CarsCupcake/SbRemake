package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;


public class EnchantedObsidian implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(EnchantedCoal.class);
    }

    @Override
    public String collection() {
        return "OBSIDIAN";
    }

    @Override
    public int level() {
        return 4;
    }

    @Override
    public String getId() {
        return "ENCHANTED_OBSIDIAN";
    }

    @Override
    public String getName() {
        return "Enchanted Obsidian";
    }

    @Override
    public Material getMaterial() {
        return Material.OBSIDIAN;
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

    @Override
    public int sellPrice() {
        return 1_440;
    }
}
