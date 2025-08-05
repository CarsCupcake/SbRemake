package me.carscupcake.sbremake.entity.impl.crimsonIsle.miniBoss;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class CrimonIsleBossEntity extends SkyblockEntity {
    public CrimonIsleBossEntity(@NotNull EntityType entityType, MobType... mobTypes) {
        super(entityType, mobTypes);
    }

    //TODO: Implement Loot Dropping
}
