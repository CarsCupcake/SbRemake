package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

public class JollyPinkRock implements ISbItem {
    @Override
    public String getId() {
        return "GIANT_FRAGMENT_BOULDER";
    }

    @Override
    public String getName() {
        return "Jolly Pink Rock";
    }

    @Override
    public Material getMaterial() {
        return Material.FIREWORK_STAR;
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

    private static final Lore lore = new Lore("""
            §8Precursor Relic
            §6\s
            §7The hottest shade of pink ever worn
            §7by a Giant.""");

    @Override
    public Lore getLore() {
        return lore;
    }
}
