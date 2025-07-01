package me.carscupcake.sbremake.item.impl.other.slayer.zombie;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class BeheadedHorror implements ISbItem, HeadWithValue, NpcSellable {
    @Override
    public String getId() {
        return "BEHEADED_HORROR";
    }

    @Override
    public String getName() {
        return "Beheaded Horror";
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
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGJhZDk5ZWQzYzgyMGI3OTc4MTkwYWQwOGE5MzRhNjhkZmE5MGQ5OTg2ODI1ZGExYzk3ZjZmMjFmNDlhZDYyNiJ9fX0=";
    }

    @Override
    public int sellPrice() {
        return 15_000;
    }
}
