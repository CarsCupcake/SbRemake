package me.carscupcake.sbremake.item.impl.other.slayer.zombie;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemStackModifiers;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class FoulFlesh implements ISbItem {
    @Override
    public String getId() {
        return "FOUL_FLESH";
    }

    @Override
    public String getName() {
        return "Foul Flesh";
    }

    @Override
    public Material getMaterial() {
        return Material.CHARCOAL;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }
}
