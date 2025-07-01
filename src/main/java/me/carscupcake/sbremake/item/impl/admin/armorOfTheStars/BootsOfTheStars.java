package me.carscupcake.sbremake.item.impl.admin.armorOfTheStars;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class BootsOfTheStars implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "STAR_BOOTS";
    }

    @Override
    public String getName() {
        return "Boots Of The Stars";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_BOOTS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Boots;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 7_500, Stat.Defense, 2_500, Stat.Intelligence, 1_000);
    }
}
