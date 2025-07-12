package me.carscupcake.sbremake.item.impl.other.foraging;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedBirchWood implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.BIRCH_LOG);
    }

    @Override
    public String collection() {
        return "BIRCH_WOOD";
    }

    @Override
    public int level() {
        return 6;
    }

    @Override
    public String getId() {
        return "ENCHANTED_BIRCH_LOG";
    }

    @Override
    public String getName() {
        return "Enchanted Birch Wood";
    }

    @Override
    public Material getMaterial() {
        return Material.BIRCH_LOG;
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
    public int sellPrice() {
        return 320;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }
}
