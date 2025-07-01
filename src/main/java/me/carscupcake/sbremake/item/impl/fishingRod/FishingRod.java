package me.carscupcake.sbremake.item.impl.fishingRod;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.IFishingRod;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class FishingRod implements ISbItem, IFishingRod, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "FISHING_ROD";
    }

    @Override
    public String getName() {
        return "Fishing Rod";
    }

    @Override
    public Material getMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public ItemType getType() {
        return ItemType.FishingRod;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 15, Stat.Strength, 15, Stat.FishingSpeed, 5, Stat.SeaCreatureChance, 1);
    }
}
