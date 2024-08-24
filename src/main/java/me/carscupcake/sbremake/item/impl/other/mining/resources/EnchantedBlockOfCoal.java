package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;


public class EnchantedBlockOfCoal implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(EnchantedCoal.class);
    }

    @Override
    public String collection() {
        return "COAL";
    }

    @Override
    public int level() {
        return 7;
    }

    @Override
    public String getId() {
        return "ENCHANTED_COAL_BLOCK";
    }

    @Override
    public String getName() {
        return "Enchanted Block Of Coal";
    }

    @Override
    public Material getMaterial() {
        return Material.COAL_BLOCK;
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
    public int sellPrice() {
        return 51_000;
    }
}
