package me.carscupcake.sbremake.item.impl.other.combat;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedRottenFlesh implements ISbItem, EnchantedRecipe, NpcSellable {
    @Override
    public ISbItem base() {
        return ISbItem.get(Material.ROTTEN_FLESH);
    }

    @Override
    public String collection() {
        return "ROTTEN_FLESH";
    }

    @Override
    public int level() {
        return 4;
    }

    @Override
    public String getId() {
        return "ENCHANTED_ROTTEN_FLESH";
    }

    @Override
    public String getName() {
        return "Enchanted Rotten Flesh";
    }

    @Override
    public Material getMaterial() {
        return Material.ROTTEN_FLESH;
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