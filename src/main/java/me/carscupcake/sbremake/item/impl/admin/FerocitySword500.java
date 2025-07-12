package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class FerocitySword500 implements ISbItem {
    @Override
    public String getId() {
        return "FEROCITY_SWORD_500";
    }

    @Override
    public String getName() {
        return "Ferocity Sword 500";
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
        if (stat == Stat.Damage || stat == Stat.Strength|| stat == Stat.CritDamage|| stat == Stat.CritChance) return 300;
        if (stat == Stat.Ferocity) return 500;
        return ISbItem.super.getStat(stat);
    }
}
