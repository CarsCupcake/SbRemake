package me.carscupcake.sbremake.item.impl.armor.growthArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.Map;

public class LeggingsOfGrowth implements ISbItem, ISbItem.StatProvider, NpcSellable, ColoredLeather {
    @Override
    public String getId() {
        return "GROWTH_LEGGINGS";
    }

    @Override
    public String getName() {
        return "Leggings of Growth";
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
        return ItemRarity.RARE;
    }

    private final Map<Stat, Number> stats = Map.of(Stat.Health, 80, Stat.Defense, 40);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public int sellPrice() {
        return 70_000;
    }


    @Override
    public Color color() {
        return HelmetOfGrowth.COLOR;
    }
}
