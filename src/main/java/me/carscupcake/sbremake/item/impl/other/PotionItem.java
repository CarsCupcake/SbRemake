package me.carscupcake.sbremake.item.impl.other;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

import static me.carscupcake.sbremake.item.ItemType.Potion;

public class PotionItem implements ISbItem {
    @Override
    public String getId() {
        return "POTION";
    }

    @Override
    public String getName() {
        return "Water Bottle";
    }

    @Override
    public Material getMaterial() {
        return Material.POTION;
    }

    @Override
    public ItemType getType() {
        return Potion;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }
}
