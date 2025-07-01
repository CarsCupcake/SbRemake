package me.carscupcake.sbremake.item.crafting;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;

public record CraftingIngredient(int amount, ISbItem... items) {
    public boolean check(SbItemStack item) {
        for (ISbItem i : items)
            if (item.sbItem() == i)
                return amount <= item.item().amount();
        return false;
    }

    public boolean contains(SbItemStack item) {
        for (ISbItem sbItem : items)
            if (item.sbItem() == sbItem) return true;
        return false;
    }
}
