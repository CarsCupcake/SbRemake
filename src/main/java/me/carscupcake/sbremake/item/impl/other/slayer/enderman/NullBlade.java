package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemStackModifiers;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class NullBlade implements ISbItem {
    @Override
    public String getId() {
        return "NULL_BLADE";
    }

    @Override
    public String getName() {
        return "Null Blade";
    }

    @Override
    public Material getMaterial() {
        return Material.SHEARS;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }
}
