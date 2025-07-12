package me.carscupcake.sbremake.item.impl.bow;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.NpcSellable;
import net.minestom.server.item.Material;

public class DecentBow implements ISbItem, BowItem, NpcSellable {
    @Override
    public String getId() {
        return "DECENT_BOW";
    }

    @Override
    public String getName() {
        return "Decent Bow";
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
        return ItemRarity.UNCOMMON;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 45;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public int sellPrice() {
        return 99;
    }
}
