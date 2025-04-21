package me.carscupcake.sbremake.item.impl.other.crimsonIsle;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class BlazeAshes implements ISbItem, NpcSellable, HeadWithValue {
    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFlMDI5YThlZWVhODBiYTNhMjc0MDdhOWYwZjIyNmI1ZWMyM2MzNTZkMzIzNTM3ZGU0ODI2OTYwZjRkNGU3OCJ9fX0=";

    @Override
    public String getId() {
        return "BLAZE_ASHES";
    }

    @Override
    public String getName() {
        return "Blaze Ashes";
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
        return ItemRarity.UNCOMMON;
    }

    @Override
    public int sellPrice() {
        return 50;
    }

    @Override
    public String value() {
        return HEAD;
    }
}
