package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;


public class EnchantedCoal implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.COAL);
    }

    @Override
    public String collection() {
        return "COAL";
    }

    @Override
    public int level() {
        return 4;
    }

    @Override
    public String getId() {
        return "ENCHANTED_COAL";
    }

    @Override
    public String getName() {
        return "Enchanted Coal";
    }

    @Override
    public Material getMaterial() {
        return Material.COAL;
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
        return 320;
    }
}
