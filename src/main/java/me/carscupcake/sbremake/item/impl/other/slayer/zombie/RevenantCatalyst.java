package me.carscupcake.sbremake.item.impl.other.slayer.zombie;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class RevenantCatalyst implements ISbItem, NpcSellable, HeadWithValue {
    @Override
    public String getId() {
        return "REVENANT_CATALYST";
    }

    @Override
    public String getName() {
        return "Revenant Catalyst";
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
        return ItemRarity.EPIC;
    }

    @Override
    public int sellPrice() {
        return 8_000;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg4Y2ZhZmE1ZjAzZjhhZWYwNDJhMTQzNzk5ZTk2NDM0MmRmNzZiN2MxZWI0NjFmNjE4ZTM5OGY4NGE5OWE2MyJ9fX0=";
    }
}
