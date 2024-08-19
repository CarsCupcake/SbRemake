package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.NpcSellable;
import net.minestom.server.item.Material;

public class DiamondSword implements ISbItem, NpcSellable {
    @Override
    public String getId() {
        return "DIAMOND_SWORD";
    }

    @Override
    public String getName() {
        return "Diamond Sword";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public int sellPrice() {
        return 8;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 35;
        return ISbItem.super.getStat(stat);
    }
}
