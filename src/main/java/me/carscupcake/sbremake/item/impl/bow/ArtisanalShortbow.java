package me.carscupcake.sbremake.item.impl.bow;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class ArtisanalShortbow implements ISbItem, Shortbow {
    @Override
    public String getId() {
        return "ARTISANAL_SHORTBOW";
    }

    @Override
    public String getName() {
        return "Artisanal Shortbow";
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
        return ItemRarity.RARE;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 40;
        return ISbItem.super.getStat(stat);
    }
}
