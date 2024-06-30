package me.carscupcake.sbremake.item.impl.other.mining.resources;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import net.minestom.server.item.Material;

public class Mithril implements ISbItem {
    @Override
    public String getId() {
        return "MITHRIL_ORE";
    }

    @Override
    public String getName() {
        return "Mithril";
    }

    @Override
    public Material getMaterial() {
        return Material.PRISMARINE_CRYSTALS;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public Lore getLore() {
        return new Lore("§7§o\"The Man called it \"true-silver\" while the Dwarves, who loved it above all things, had their own, secret name for it.\"");
    }
}
