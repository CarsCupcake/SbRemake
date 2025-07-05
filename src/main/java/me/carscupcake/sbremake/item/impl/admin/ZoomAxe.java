package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemStackModifiers;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import net.minestom.server.item.Material;

import java.util.Map;

public class ZoomAxe implements ISbItem {
    @Override
    public String getId() {
        return "ZOOM_AXE";
    }

    @Override
    public String getName() {
        return "Zoom";
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_AXE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Axe;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Sweep || stat == Stat.Damage) return 99999999;
        return ISbItem.super.getStat(stat);
    }

    private final ItemStackModifiers modifiers = ItemStackModifiers.builder().enchantments(Map.of(NormalEnchantments.Efficiency, 10)).build();

    @Override
    public ItemStackModifiers modifierBuilder() {
        return modifiers;
    }
}
