package me.carscupcake.sbremake.item.smithing;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public interface SmithingItem {
    SbItemStack onApply(SkyblockPlayer player, SbItemStack left, SbItemStack right);
    boolean canBeApplied(SkyblockPlayer player, SbItemStack other);
}
