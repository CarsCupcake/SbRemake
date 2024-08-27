package me.carscupcake.sbremake.item.modifiers.gemstone;

import me.carscupcake.sbremake.util.Cost;

/**
 * Interface is used to set the Gemstone Slots for a Sb Item
 */
public interface GemstoneSlots {
    GemstoneSlotType[] getGemstoneSlots();
    boolean[] getUnlocked();
    Cost[][] getLockedSlotCost();
}
