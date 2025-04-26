package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

public class DiamanteHandles implements ISbItem {
    @Override
    public String getId() {
        return "GIANT_FRAGMENT_DIAMOND";
    }

    @Override
    public String getName() {
        return "Diamante's Handle";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_HORSE_ARMOR;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public boolean isDungeonItem() {
        return true;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    private static final Lore lore = new Lore("""
            §8Precursor Relic
            §6\s
            §7The hilt of the largest sword known
            §7to humankind.""");

    @Override
    public Lore getLore() {
        return lore;
    }
}
