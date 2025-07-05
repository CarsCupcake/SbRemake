package me.carscupcake.sbremake.item.impl.other.foraging;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedDarkOakWood implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.DARK_OAK_LOG);
    }

    @Override
    public String collection() {
        return "DARK_OAK_WOOD";
    }

    @Override
    public int level() {
        return 3;
    }

    @Override
    public String getId() {
        return "ENCHANTED_DARK_OAK_LOG";
    }

    @Override
    public String getName() {
        return "Enchanted Dark Oak Wood";
    }

    @Override
    public Material getMaterial() {
        return Material.DARK_OAK_LOG;
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
