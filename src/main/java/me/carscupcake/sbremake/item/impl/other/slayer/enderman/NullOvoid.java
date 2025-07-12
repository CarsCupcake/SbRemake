package me.carscupcake.sbremake.item.impl.other.slayer.enderman;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemStackModifiers;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class NullOvoid implements ISbItem {
    @Override
    public String getId() {
        return "NULL_OVOID";
    }

    @Override
    public String getName() {
        return "Null Ovoid";
    }

    @Override
    public Material getMaterial() {
        return Material.ENDERMAN_SPAWN_EGG;
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
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }
}
