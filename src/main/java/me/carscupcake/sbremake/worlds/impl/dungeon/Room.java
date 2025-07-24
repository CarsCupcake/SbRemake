package me.carscupcake.sbremake.worlds.impl.dungeon;

import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.worlds.impl.dungeon.runtime.RoomData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Room(RoomType type, RoomShape shape, Pos2d pos, Rotation rotation, List<Pos2d> children, @Nullable Pos2d parent) {
    public Room withRotation(Rotation rotation) {
        return new Room(type, shape, pos, rotation, children, parent);
    }
}
