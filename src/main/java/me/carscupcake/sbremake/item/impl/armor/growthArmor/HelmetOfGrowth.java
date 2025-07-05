package me.carscupcake.sbremake.item.impl.armor.growthArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.Map;

public class HelmetOfGrowth implements ISbItem, ISbItem.StatProvider, NpcSellable, ColoredLeather {
    public static final Color COLOR = new Color(0x00be00);
    @Override
    public String getId() {
        return "GROWTH_HELMET";
    }

    @Override
    public String getName() {
        return "Helmet of Growth";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_HELMET;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    private final Map<Stat, Number> stats = Map.of(Stat.Health, 50, Stat.Defense, 30);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public int sellPrice() {
        return 50_000;
    }

    @Override
    public Color color() {
        return COLOR;
    }
}
