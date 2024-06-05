package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.bow.BowItem;
import net.minestom.server.item.Material;

public class Raygun implements ISbItem, BowItem {
    @Override
    public String getId() {
        return "RAYGUN";
    }

    @Override
    public String getName() {
        return "Raygun";
    }

    @Override
    public Material getMaterial() {
        return Material.BOW;
    }

    @Override
    public ItemType getType() {
        return ItemType.Bow;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public int arrowsToShoot(SbItemStack stack) {
        return 5;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 99999999;
        return ISbItem.super.getStat(stat);
    }
}
