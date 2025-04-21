package me.carscupcake.sbremake.item.impl.other.crimsonIsle;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class MutatedBLazeAshes implements ISbItem, HeadWithValue, NpcSellable {
    private static final String HEAD = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTMwNjhhMTE2ZjY2ZDRjZjEzYjVmZWJlMjM3MTkxODY0MjFiY2U2YThhYjUxZTMyMWIzYTE4MmI1ZDBmMDE2OSJ9fX0=";
    @Override
    public String value() {
        return HEAD;
    }

    @Override
    public String getId() {
        return "MUTATED_BLAZE_ASHES";
    }

    @Override
    public String getName() {
        return "Mutated Blaze Ashes";
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
        return 200;
    }
}
