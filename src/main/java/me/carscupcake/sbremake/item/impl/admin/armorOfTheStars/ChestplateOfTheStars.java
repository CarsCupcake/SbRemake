package me.carscupcake.sbremake.item.impl.admin.armorOfTheStars;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class ChestplateOfTheStars implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "STAR_CHESTPLATE";
    }

    @Override
    public String getName() {
        return "Chestplate Of The Stars";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_CHESTPLATE;
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
        return Map.of(Stat.Health, 20_000, Stat.Defense, 5_000, Stat.Intelligence, 2_000);
    }
}
