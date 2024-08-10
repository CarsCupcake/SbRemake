package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class Titanium implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return "TITANIUM_ORE";
    }

    @Override
    public String getName() {
        return "Titanium";
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
        return ItemRarity.RARE;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTE0YzZlNDFhNzYyZDM3ODYzYTlmZmY2ODg4YzczODkwNWI5MmNjNmMzODk4ODkyYTM4ZGZkZmUyYWM0YmYifX19";
    }
}
