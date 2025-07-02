package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

import java.util.Map;

public class DiamondPickaxe implements ISbItem, ISbItem.StatProvider, IVanillaPickaxe {
    @Override
    public String getId() {
        return "DIAMOND_PICKAXE";
    }

    @Override
    public String getName() {
        return "Diamond Pickaxe";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_PICKAXE;
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
        return Map.of(Stat.Damage, 30, Stat.MiningSpeed, 220, Stat.BreakingPower, 4);
    }

    @Override
    public VanillaPickaxeTier getTier() {
        return VanillaPickaxeTier.Diamond;
    }
}
