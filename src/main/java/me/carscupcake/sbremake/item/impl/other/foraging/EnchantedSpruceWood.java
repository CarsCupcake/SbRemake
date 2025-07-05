package me.carscupcake.sbremake.item.impl.other.foraging;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedSpruceWood implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.SPRUCE_LOG);
    }

    @Override
    public String collection() {
        return "SPRUCE_WOOD";
    }

    @Override
    public int level() {
        return 3;
    }

    @Override
    public String getId() {
        return "ENCHANTED_SPRUCE_LOG";
    }

    @Override
    public String getName() {
        return "Enchanted Spruce Wood";
    }

    @Override
    public Material getMaterial() {
        return Material.SPRUCE_LOG;
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
