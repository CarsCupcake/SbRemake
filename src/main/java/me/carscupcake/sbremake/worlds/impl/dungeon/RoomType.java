package me.carscupcake.sbremake.worlds.impl.dungeon;

import lombok.Getter;
import net.minestom.server.map.MapColors;

public enum RoomType {
    Entrance(true),
    Room(false),
    Blood(true),
    Fairy(true),
    Puzzle(false),
    Trap(false),
    Mini(false);
    @Getter
    private final boolean isSpecial;
    RoomType(boolean isSpecial) {
        this.isSpecial = isSpecial;
    }
}
