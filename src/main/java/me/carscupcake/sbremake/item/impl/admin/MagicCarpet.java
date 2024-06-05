package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

import java.util.Map;

public class MagicCarpet implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "MAGIC_CARPET";
    }

    @Override
    public String getName() {
        return "Magic Carpet";
    }

    @Override
    public Material getMaterial() {
        return Material.RED_CARPET;
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
    public Map<Stat, Double> stats() {
        return Map.of(Stat.Vitality, 100_000d, Stat.Mending, 100_000d);
    }

    @Override
    public Lore getLore() {
        return new Lore("§d§oElevates §7your healing to the moon!");
    }
}
