package me.carscupcake.sbremake.item.impl.other.foraging;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedAcaciaWood implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.ACACIA_LOG);
    }

    @Override
    public String collection() {
        return "ACACIA_WOOD";
    }

    @Override
    public int level() {
        return 6;
    }

    @Override
    public String getId() {
        return "ENCHANTED_ACACIA_LOG";
    }

    @Override
    public String getName() {
        return "Enchanted Acacia Wood";
    }

    @Override
    public Material getMaterial() {
        return Material.ACACIA_LOG;
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
