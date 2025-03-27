package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.player.SkyblockPlayer;

public interface Requirement {
    boolean hasRequirement(SkyblockPlayer player);

    String message(SkyblockPlayer player);
}
