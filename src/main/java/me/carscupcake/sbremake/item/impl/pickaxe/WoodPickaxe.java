package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class WoodPickaxe implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "WOODEN_PICKAXE";
    }

    @Override
    public String getName() {
        return "Wooden Pickaxe";
    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_PICKAXE;
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
        return Map.of(Stat.Damage, 15, Stat.MiningSpeed, 70, Stat.BreakingPower, 1);
    }
}
