package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemStackModifiers;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.impl.bow.Bow;
import me.carscupcake.sbremake.item.impl.bow.BowItem;
import net.minestom.server.item.Material;

import java.util.HashMap;
import java.util.Map;

public class AdminBow implements ISbItem, BowItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "ADMIN_BOW";
    }

    @Override
    public String getName() {
        return "Admin Bow";
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
        return ItemRarity.ADMIN;
    }

    private static final Map<Stat, Number> stats = Map.of(Stat.Damage, 10_000, Stat.CritChance, 5_000);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }
}
