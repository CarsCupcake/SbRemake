package me.carscupcake.sbremake.item.impl.rune;

import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public non-sealed interface ArmorRune extends IRune<SkyblockPlayer> {
    ItemType[] allowedArmor();
}
