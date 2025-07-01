package me.carscupcake.sbremake.item.impl.armor.crimsonIsle;

import me.carscupcake.sbremake.item.HeadWithValue;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public abstract class KuudraHelmetBaseline extends KuudraArmorCommons implements HeadWithValue {

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }
}
