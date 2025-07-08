package me.carscupcake.sbremake.item.impl.armor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.NpcSellable;
import net.minestom.server.item.Material;

public class ZombieHelmet implements ISbItem, NpcSellable {
    @Override
    public String getId() {
        return "ZOMBIE_HAT";
    }

    @Override
    public String getName() {
        return "Zombie Hat";
    }

    @Override
    public Material getMaterial() {
        return Material.ZOMBIE_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public int sellPrice() {
        return 8;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) return 10;
        return ISbItem.super.getStat(stat);
    }
}
