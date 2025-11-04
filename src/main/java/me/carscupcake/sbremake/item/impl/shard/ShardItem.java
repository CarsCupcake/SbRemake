package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import net.minestom.server.item.Material;

public class ShardItem implements ISbItem {
    @Override
    public String getId() {
        return "ATTRIBUTE_SHARD";
    }

    @Override
    public String getName() {
        return "Shard";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Wand;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.SPECIAL;
    }
}
