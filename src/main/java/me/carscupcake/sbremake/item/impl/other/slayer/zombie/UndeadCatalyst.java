package me.carscupcake.sbremake.item.impl.other.slayer.zombie;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class UndeadCatalyst implements ISbItem, NpcSellable, HeadWithValue {
    @Override
    public String getId() {
        return "UNDEAD_CATALYST";
    }

    @Override
    public String getName() {
        return "Undead Catalyst";
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
    public int sellPrice() {
        return 2_000;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODA2MjUzNjliMGE3YjA1MjYzMmRiNmI5MjZhODc2NzAyMTk1Mzk5MjI4MzZhYzU5NDBiZTI2ZDM0YmYxNGUxMCJ9fX0=";
    }
}
