package me.carscupcake.sbremake.item.impl.armor.lapisArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ColoredLeather;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

public class LapisBoots implements ISbItem, ColoredLeather {
    @Override
    public String getId() {
        return "LAPIS_ARMOR_BOOTS";
    }

    @Override
    public String getName() {
        return "Lapis Armor Boots";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Boots;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) return 20;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public Color color() {
        return new Color(0x0000ff);
    }
}