package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class EnchantedMithril implements ISbItem, EnchantedRecipe {
    @Override
    public String getId() {
        return "ENCHANTED_MITHRIL";
    }

    @Override
    public String getName() {
        return "Enchanted Mithril";
    }

    @Override
    public Material getMaterial() {
        return Material.PRISMARINE_CRYSTALS;
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
    public ISbItem base() {
        return ISbItem.get(Mithril.class);
    }

    @Override
    public String collection() {
        return "MITHRIL";
    }

    @Override
    public int level() {
        return 3;
    }
}
