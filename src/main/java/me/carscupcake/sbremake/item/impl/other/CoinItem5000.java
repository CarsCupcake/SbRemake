package me.carscupcake.sbremake.item.impl.other;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class CoinItem5000 implements ISbItem, HeadWithValue, ICoinItem {
    @Override
    public String getId() {
        return "COIN_ITEM_5000";
    }

    @Override
    public String getName() {
        return "Hod did you get this?";
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
        return ItemRarity.SPECIAL;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTQxMmQyZWMzZDQ1ODEyY2ZkYWMwYjg1NmRlODdmMWQzODM4YzFjNTA5ODM2ZmYyMWEwYTVjZDQ3Yzc5MDY5ZiJ9fX0=";
    }

    @Override
    public int coinValue() {
        return 5000;
    }
}
