package me.carscupcake.sbremake.worlds.region;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;

public interface Region {
    boolean isInRegion(Point pos);

    default void onEnter(SkyblockPlayer player) {
        //First enter message
    }

    default void onExit(SkyblockPlayer player) {

    }

    String name();
}
