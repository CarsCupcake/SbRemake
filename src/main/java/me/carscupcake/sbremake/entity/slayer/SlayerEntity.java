package me.carscupcake.sbremake.entity.slayer;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class SlayerEntity extends SkyblockEntity {
    public SlayerEntity(@NotNull EntityType entityType, LootTable<SbItemStack> lootTable) {
        super(entityType, lootTable);
    }
}
