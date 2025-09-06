package me.carscupcake.sbremake.entity.impl.dungeon.melee;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.impl.dungeon.DungeonEntity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.impl.dungeon.runtime.RoomData;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CryptDreadlord extends DungeonEntity {
    public CryptDreadlord(int floor, boolean mastermode, boolean starmob, RoomData roomData) {
        super(EntityType.PLAYER, floor, mastermode, starmob, roomData, new LootTable<>(), MobType.Undead);
    }

    @Override
    public float getMaxHealth() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }
}
