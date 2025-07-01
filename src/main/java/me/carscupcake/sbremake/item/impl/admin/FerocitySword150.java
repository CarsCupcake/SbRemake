package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class FerocitySword150 implements ISbItem {
    @Override
    public String getId() {
        return "FEROCITY_SWORD_150";
    }

    @Override
    public String getName() {
        return "Ferocity Sword 150";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 10;
        if (stat == Stat.Ferocity) return 150;
        return ISbItem.super.getStat(stat);
    }
}
