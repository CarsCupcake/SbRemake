package me.carscupcake.sbremake.item.impl.other.foraging;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedOakWood implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.OAK_LOG);
    }

    @Override
    public String collection() {
        return "OAK_WOOD";
    }

    @Override
    public int level() {
        return 3;
    }

    @Override
    public String getId() {
        return "ENCHANTED_OAK_LOG";
    }

    @Override
    public String getName() {
        return "Enchanted Oak Wood";
    }

    @Override
    public Material getMaterial() {
        return Material.OAK_LOG;
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
