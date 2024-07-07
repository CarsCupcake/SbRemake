package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class IronPickaxe implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "IRON_PICKAXE";
    }

    @Override
    public String getName() {
        return "Iron Pickaxe";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_PICKAXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Pickaxe;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 25, Stat.MiningSpeed, 160, Stat.BreakingPower, 3);
    }
}
