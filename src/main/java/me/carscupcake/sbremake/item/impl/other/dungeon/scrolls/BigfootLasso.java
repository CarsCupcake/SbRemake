package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.*;
import net.minestom.server.item.Material;

public class BigfootLasso implements ISbItem {
    @Override
    public String getId() {
        return "GIANT_FRAGMENT_BIGFOOT";
    }

    @Override
    public String getName() {
        return "Bigfoot's Lasso";
    }

    @Override
    public Material getMaterial() {
        return Material.LEAD;
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
            §7Hard to throw, even harder to catch.""");

    @Override
    public Lore getLore() {
        return lore;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }
}
