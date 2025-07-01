package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class TitansChestplate implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "TITANS_CHESTPLATE";
    }

    @Override
    public String getName() {
        return "Titan's Chestplate";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Chestplate;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 300d, Stat.Defense, 200d, Stat.Intelligence, 75d, Stat.Strength, 20d);
    }
}
