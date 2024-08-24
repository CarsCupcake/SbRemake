package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;


public class EnchantedCobblestone implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.COBBLESTONE);
    }

    @Override
    public String collection() {
        return "COBBLESTONE";
    }

    @Override
    public int level() {
        return 4;
    }

    @Override
    public String getId() {
        return "ENCHANTED_COBBLESTONE";
    }

    @Override
    public String getName() {
        return "Enchanted Cobblestone";
    }

    @Override
    public Material getMaterial() {
        return Material.COBBLESTONE;
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
        return 160;
    }
}
