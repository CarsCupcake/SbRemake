package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

import java.util.Map;

public class FracturedMithrilPickaxe implements ISbItem, ISbItem.StatProvider, IVanillaPickaxe {
    @Override
    public String getId() {
        return "FRACTURED_MITHRIL_PICKAXE";
    }

    @Override
    public String getName() {
        return "Fractured Mithril Pickaxe";
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
        return ItemRarity.UNCOMMON;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 30d, Stat.MiningSpeed, 225d, Stat.BreakingPower, 5d);
    }

    @Override
    public VanillaPickaxeTier getTier() {
        return VanillaPickaxeTier.Stone;
    }
}
