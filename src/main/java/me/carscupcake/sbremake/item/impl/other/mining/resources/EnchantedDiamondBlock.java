package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedDiamondBlock implements ISbItem, EnchantedRecipe {
    @Override
    public String getId() {
        return "ENCHANTED_DIAMOND_BLOCK";
    }

    @Override
    public String getName() {
        return "Enchanted Diamond Block";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_BLOCK;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.builder().glint(true).build();
    }

    @Override
    public ISbItem base() {
        return ISbItem.get(EnchantedDiamond.class);
    }

    @Override
    public String collection() {
        return "DIAMOND";
    }

    @Override
    public int level() {
        return 8;
    }
}
