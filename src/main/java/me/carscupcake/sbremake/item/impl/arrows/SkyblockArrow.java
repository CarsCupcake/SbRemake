package me.carscupcake.sbremake.item.impl.arrows;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.item.Material;

public interface SkyblockArrow extends ISbItem {
    void onEntityHit(SkyblockEntity entity, SkyblockPlayer shooter);

    @Override
    default Material getMaterial() {
        return Material.ARROW;
    }

    @Override
    default ItemType getType() {
        return ItemType.Arrow;
    }
}
