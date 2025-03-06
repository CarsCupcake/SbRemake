package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

import java.util.Map;

public class StonePickaxe implements ISbItem, ISbItem.StatProvider, IVanillaPickaxe {
    @Override
    public String getId() {
        return "STONE_PICKAXE";
    }

    @Override
    public String getName() {
        return "Stone Pickaxe";
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_PICKAXE;
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
        return Map.of(Stat.Damage, 20, Stat.MiningSpeed, 110, Stat.BreakingPower, 2);
    }

    @Override
    public VanillaPickaxeTier getTier() {
        return VanillaPickaxeTier.Stone;
    }
}
