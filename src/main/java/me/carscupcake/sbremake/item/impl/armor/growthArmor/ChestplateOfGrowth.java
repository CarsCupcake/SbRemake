package me.carscupcake.sbremake.item.impl.armor.growthArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.Map;

public class ChestplateOfGrowth implements ISbItem, ISbItem.StatProvider, NpcSellable, ColoredLeather {
    @Override
    public String getId() {
        return "GROWTH_CHESTPLATE";
    }

    @Override
    public String getName() {
        return "Chestplate of Growth";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Chestplate;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    private final Map<Stat, Number> stats = Map.of(Stat.Health, 100, Stat.Defense, 50);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public int sellPrice() {
        return 80_000;
    }


    @Override
    public Color color() {
        return HelmetOfGrowth.COLOR;
    }
}
