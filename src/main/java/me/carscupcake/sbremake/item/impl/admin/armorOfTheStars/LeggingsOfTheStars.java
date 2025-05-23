package me.carscupcake.sbremake.item.impl.admin.armorOfTheStars;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class LeggingsOfTheStars implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "STAR_LEGGINGS";
    }

    @Override
    public String getName() {
        return "Leggings Of The Stars";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_LEGGINGS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Leggings;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 15_000, Stat.Defense, 4_000, Stat.Intelligence, 1_500);
    }
}
