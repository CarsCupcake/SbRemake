package me.carscupcake.sbremake.worlds.impl.dungeon.runtime;

import lombok.Getter;
import me.carscupcake.sbremake.worlds.impl.dungeon.Room;
import me.carscupcake.sbremake.worlds.impl.dungeon.RoomType;
import net.minestom.server.instance.Instance;

@Getter
public class RoomData {
    private boolean discovered;
    private final String id;
    private final Room room;
    public RoomData(Instance instance, String id, Room room) {
        this.id = id;
        this.room = room;
        this.discovered = room.type() != RoomType.Entrance;
    }
}
