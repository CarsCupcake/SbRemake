package me.carscupcake.sbremake.blocks;

import me.carscupcake.sbremake.item.ItemType;

public enum MiningType {
    Pickaxe(new ItemType[]{ItemType.Pickaxe, ItemType.Drill}),
    Shovel(new ItemType[]{ItemType.Shovel});
    private final ItemType[] allowed;

    MiningType(ItemType[] allowed) {
        this.allowed = allowed;
    }

    public boolean allowed(ItemType type) {
        for (ItemType itemType : allowed)
            if (itemType == type) return true;
        return false;
    }
}
