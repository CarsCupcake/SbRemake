package me.carscupcake.sbremake.entity.impl.crimsonIsle.miniBoss;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public abstract class CrimonIsleBossEntity extends SkyblockEntity {
    public CrimonIsleBossEntity(@NotNull EntityType entityType) {
        super(entityType);
    }

    //TODO: Implement Loot Dropping
}
