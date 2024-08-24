package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedDiamond implements ISbItem, EnchantedRecipe {
    @Override
    public String getId() {
        return "ENCHANTED_DIAMOND";
    }

    @Override
    public String getName() {
        return "Enchanted Diamond";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND;
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
    public ISbItem base() {
        return ISbItem.get(Material.DIAMOND);
    }

    @Override
    public String collection() {
        return "DIAMOND";
    }

    @Override
    public int level() {
        return 4;
    }
}
