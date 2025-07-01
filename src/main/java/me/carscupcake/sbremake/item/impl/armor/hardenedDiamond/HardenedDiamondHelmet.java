package me.carscupcake.sbremake.item.impl.armor.hardenedDiamond;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.NpcSellable;
import net.minestom.server.item.Material;

public class HardenedDiamondHelmet implements ISbItem, NpcSellable {
    @Override
    public String getId() {
        return "HARDENED_DIAMOND_HELMET";
    }

    @Override
    public String getName() {
        return "Hardened Diamond Helmet";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public int sellPrice() {
        return 3_200;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) return 60;
        return ISbItem.super.getStat(stat);
    }
}
