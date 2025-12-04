package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.worlds.WarpLocation;
import me.carscupcake.sbremake.worlds.impl.Dungeon;
import me.carscupcake.sbremake.worlds.impl.dungeon.*;
import net.minestom.server.command.builder.Command;

import java.util.ArrayList;

public class LaunchPrefilledDungeonCOmmand extends Command {
    public LaunchPrefilledDungeonCOmmand() {
        super("prefilleddungeon");
        setDefaultExecutor((sender, context) -> {
            var room = new Room(RoomType.Room, RoomShape.ONE_BY_ONE, new Pos2d(0, 0), Rotation.NW, new ArrayList<>(), null);
            Room[][] rooms = {
                    {
                            room, room.withPos(new Pos2d(1, 0)), room.withPos(new Pos2d(2, 0)), room.withPos(new Pos2d(3, 0)), room.withPos(new Pos2d(4, 0)), room.withPos(new Pos2d(5, 0))
                    },
                    {
                            room.withPos(new Pos2d(0, 1)), room.withPos(new Pos2d(1, 1)), room.withPos(new Pos2d(2, 1)), room.withPos(new Pos2d(3, 1)), room.withPos(new Pos2d(4, 1)), room.withPos(new Pos2d(5, 1))
                    },
                    {
                            room.withPos(new Pos2d(0, 2)), room.withPos(new Pos2d(1, 2)), room.withPos(new Pos2d(2, 2)), room.withPos(new Pos2d(3, 2)), room.withPos(new Pos2d(4, 2)), room.withPos(new Pos2d(5, 2))
                    },
                    {
                            room.withPos(new Pos2d(0, 3)), room.withPos(new Pos2d(1, 3)), room.withPos(new Pos2d(2, 3)), room.withPos(new Pos2d(3, 3)), room.withPos(new Pos2d(4, 3)), room.withPos(new Pos2d(5, 3))
                    },
                    {
                            room.withPos(new Pos2d(0, 4)), room.withPos(new Pos2d(1, 4)), room.withPos(new Pos2d(2, 4)), room.withPos(new Pos2d(3, 4)), room.withPos(new Pos2d(4, 4)), room.withPos(new Pos2d(5, 4))
                    },
                    {
                            room.withPos(new Pos2d(0, 5)), room.withPos(new Pos2d(1, 5)), room.withPos(new Pos2d(2, 5)), room.withPos(new Pos2d(3, 5)), room.withPos(new Pos2d(4, 5)), room.withPos(new Pos2d(5, 5))
                    }
            };
            var generator = new Generator(rooms, -1);
            for (int x = 0; x < rooms.length; x++) {
                for (int y = 0; y < rooms[x].length; y++) {
                    if (x != rooms.length - 1)
                        generator.getDoorsHorizontal()[x][y] = DoorType.None;
                    if (y != rooms[x].length - 1)
                        generator.getDoorsVertical()[x][y] = DoorType.None;

                }
            }
            generator.setRooms(rooms);
            Dungeon dungeon = new Dungeon(generator);
            var player = ((SkyblockPlayer) sender);
            dungeon.init(() -> {
                synchronized (player) {
                    player.setWorldProvider(dungeon, WarpLocation.Dungeon);
                }
            });
        });
    }
}
