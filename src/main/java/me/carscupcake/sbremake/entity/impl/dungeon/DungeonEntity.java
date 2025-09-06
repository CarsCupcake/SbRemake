package me.carscupcake.sbremake.entity.impl.dungeon;

import lombok.Getter;
import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.impl.dungeon.runtime.RoomData;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class DungeonEntity extends SkyblockEntity {
    private final int floor;
    private final boolean mastermode;
    private final boolean starmob;
    private final RoomData roomData;
    public DungeonEntity(@NotNull EntityType entityType, int floor, boolean mastermode, boolean starmob, RoomData roomData, LootTable<SbItemStack> lootTable, MobType... mobTypes) {
        super(entityType, lootTable, mobTypes);
        this.floor = floor;
        this.mastermode = mastermode;
        this.starmob = starmob;
        this.roomData = roomData;
    }
}
