package me.carscupcake.sbremake.item.impl.armor.lapisArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ColoredLeather;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.ability.Ability;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.List;

public class LapisLeggings implements ISbItem, ColoredLeather {
    @Override
    public String getId() {
        return "LAPIS_ARMOR_LEGGINGS";
    }

    @Override
    public String getName() {
        return "Lapis Armor Leggings";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_LEGGINGS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Leggings;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) return 35;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public Color color() {
        return new Color(0x0000ff);
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(HealthFullSetBonus.INSTANCE);
    }
}
