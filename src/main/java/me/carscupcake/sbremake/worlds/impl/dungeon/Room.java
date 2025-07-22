package me.carscupcake.sbremake.worlds.impl.dungeon;

import me.carscupcake.sbremake.util.Pos2d;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record Room(RoomType type, RoomShape shape, Pos2d pos, Rotation rotation, List<Pos2d> children, @Nullable Pos2d parent) {
}
