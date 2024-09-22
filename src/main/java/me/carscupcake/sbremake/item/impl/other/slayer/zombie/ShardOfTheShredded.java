package me.carscupcake.sbremake.item.impl.other.slayer.zombie;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class ShardOfTheShredded implements ISbItem, HeadWithValue {
    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzBjNWNjNzI4Yzg2OWVjZjNjNmUwOTc5ZThhYTA5YzEwMTQ3ZWQ3NzA0MTdlNGJhNTQxYWFjMzgyZjAifX19";
    }

    @Override
    public String getId() {
        return "SHARD_OF_THE_SHREDDED";
    }

    @Override
    public String getName() {
        return "Shard Of The Shredded";
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
}
