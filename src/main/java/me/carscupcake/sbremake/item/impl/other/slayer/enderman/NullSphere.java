package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemStackModifiers;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class NullSphere implements ISbItem {
    @Override
    public String getId() {
        return "NULL_SPHERE";
    }

    @Override
    public String getName() {
        return "Null Sphere";
    }

    @Override
    public Material getMaterial() {
        return Material.FIREWORK_STAR;
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
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }
}
