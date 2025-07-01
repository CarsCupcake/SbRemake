package me.carscupcake.sbremake.item.impl.armor.crimsonIsle;

import me.carscupcake.sbremake.item.ColoredLeather;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public abstract class KuudraChestplateBaseline extends KuudraArmorCommons implements ColoredLeather {

    @Override
    public Material getMaterial() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Chestplate;
    }
}
