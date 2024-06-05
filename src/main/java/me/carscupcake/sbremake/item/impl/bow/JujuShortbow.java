package me.carscupcake.sbremake.item.impl.bow;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import java.util.Map;

public class JujuShortbow implements ISbItem, Shortbow, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "JUJU_SHORTBOW";
    }

    @Override
    public String getName() {
        return "Juju Shortbow";
    }

    @Override
    public Material getMaterial() {
        return Material.BOW;
    }

    @Override
    public ItemType getType() {
        return ItemType.Bow;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public Map<Stat, Double> stats() {
        return Map.of(Stat.Damage, 310d, Stat.Strength, 40d, Stat.CritChance, 10d, Stat.CritDamage, 110d);
    }
}
