package me.carscupcake.sbremake.item.smithing;

import me.carscupcake.sbremake.item.SbItemStack;

public interface SmithingItem {
    SbItemStack onApply(SbItemStack left, SbItemStack right);
    boolean canBeApplied(SbItemStack other);
}
