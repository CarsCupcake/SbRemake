package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class Zoom implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "ZOOM_PICKAXE";
    }

    @Override
    public String getName() {
        return "Zoom";
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
        return ItemRarity.ADMIN;
    }

    @Override
    public Map<Stat, Double> stats() {
        return Map.of(Stat.Damage, 99999999d, Stat.MiningSpeed, 99999999d, Stat.BreakingPower, 10d);
    }
}
