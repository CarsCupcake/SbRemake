package me.carscupcake.sbremake.item.impl.pets;

import me.carscupcake.sbremake.config.KeyClass;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;

public interface PetItem extends ISbItem, KeyClass {
    @SuppressWarnings("unused")
    static PetItem fromKey(String key) {
        try {
            return (PetItem) SbItemStack.raw(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
