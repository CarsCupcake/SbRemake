package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class TheFast implements ISbItem, HeadWithValue {
    @Override
    public String getId() {
        return "SPEED_RACER";
    }

    @Override
    public String getName() {
        return "The Fast";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Speed) return 1000;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxNGUyNTJjYTRhMzQzODgyYzNiMDViYmNmYjhmYzgzN2QyZGU5ZGI0MWRjNTM3MTEzYjczMjA5OCJ9fX0=";
    }
}
