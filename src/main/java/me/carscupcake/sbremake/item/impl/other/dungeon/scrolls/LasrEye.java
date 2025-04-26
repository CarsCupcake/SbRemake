package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

public class LasrEye implements ISbItem {
    @Override
    public String getId() {
        return "GIANT_FRAGMENT_LASER";
    }

    @Override
    public String getName() {
        return "L.A.S.R.'s Eye";
    }

    @Override
    public Material getMaterial() {
        return Material.ENDER_EYE;
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
            §7This eye is mightier than the sword.""");

    @Override
    public Lore getLore() {
        return lore;
    }
}
