package me.carscupcake.sbremake.item.impl.admin.armorOfTheStars;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class HelmetOfTheStars implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "STAR_HELMET";
    }

    @Override
    public String getName() {
        return "Helmet Of The Stars";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_HELMET;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 10_000, Stat.Defense, 3_000, Stat.Intelligence, 1_000);
    }
}
