package me.carscupcake.sbremake.item.impl.armor.hardenedDiamond;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.NpcSellable;
import net.minestom.server.item.Material;

public class HardenedDiamondChestplate implements ISbItem, NpcSellable {
    @Override
    public String getId() {
        return "HARDENED_DIAMOND_CHESTPLATE";
    }

    @Override
    public String getName() {
        return "Hardened Diamond Chestplate";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_CHESTPLATE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Chestplate;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.RARE;
    }

    @Override
    public int sellPrice() {
        return 5_120;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) return 120;
        return ISbItem.super.getStat(stat);
    }
}
