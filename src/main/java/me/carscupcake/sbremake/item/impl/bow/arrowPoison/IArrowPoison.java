package me.carscupcake.sbremake.item.impl.bow.arrowPoison;

import me.carscupcake.sbremake.event.PlayerProjectileDamageEntityEvent;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemType;

public interface IArrowPoison extends ISbItem {
    @Override
    default ItemType getType() {
        return ItemType.ArrowPoison;
    }

    void onHit(PlayerProjectileDamageEntityEvent event);
}
