package me.carscupcake.sbremake.worlds;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityNpc extends AbstractNpc {
    private final NpcEntity entity;

    public EntityNpc(Pos pos, Instance instance, String name, EntityType type) {
        super(pos, instance, name);
        entity = new NpcEntity(type);
        entity.setCustomName(Component.text("§r%s".formatted(name)));
    }

    @Override
    public void spawn(SkyblockPlayer player) {
        entity.setInstance(player.getInstance(), getPos());
    }

    @Override
    public int getEntityId() {
        return entity.getEntityId();
    }

    @Override
    public Pos getEyePosition() {
        return getPos().add(0, entity.getEyeHeight(), 0);
    }

    private static class NpcEntity extends Entity {

        public NpcEntity(@NotNull EntityType entityType) {
            super(entityType, UUID.randomUUID());
            setCustomNameVisible(true);
        }
    }
}
