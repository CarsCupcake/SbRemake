package me.carscupcake.sbremake.item.impl.armor.growthArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.Map;

public class BootsOfGrowth implements ISbItem, ISbItem.StatProvider, NpcSellable, ColoredLeather {
    @Override
    public String getId() {
        return "GROWTH_BOOTS";
    }

    @Override
    public String getName() {
        return "Boots of Growth";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_LEGGINGS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Boots;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    private final Map<Stat, Number> stats = Map.of(Stat.Health, 50, Stat.Defense, 25);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public int sellPrice() {
        return 40_000;
    }


    @Override
    public Color color() {
        return HelmetOfGrowth.COLOR;
    }
}
