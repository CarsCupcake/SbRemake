package me.carscupcake.sbremake.item.impl.pickaxe;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.CollectionRequirement;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class ZombiePickaxe implements ISbItem, ISbItem.StatProvider, NpcSellable {
    @Override
    public String getId() {
        return "ZOMBIE_PICKAXE";
    }

    @Override
    public String getName() {
        return "Zombie Pickaxe";
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
    Map<Stat, Number> stats = Map.of(Stat.BreakingPower, 3, Stat.MiningSpeed, 190);
    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }
    List<Requirement> requirements = List.of(new CollectionRequirement("ROTTEN_FLESH", 2));
    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    @Override
    public int sellPrice() {
        return 3;
    }
}
