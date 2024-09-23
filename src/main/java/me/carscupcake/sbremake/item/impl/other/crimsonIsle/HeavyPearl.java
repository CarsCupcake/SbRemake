package me.carscupcake.sbremake.item.impl.other.crimsonIsle;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class HeavyPearl implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return "HEAVY_PEARL";
    }

    @Override
    public String getName() {
        return "Heavy Pearl";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
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
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmRiZDRlNWQzZDljMDVhMDM2ZmI2MmU2ZTcwZmFmOWU2Zjk4ZDI5NGY5ZDAwNjc4MWMxNDRjOWYxNWI4NzcxNSJ9fX0=";
    }
}
