package me.carscupcake.sbremake.worlds;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntitySpawner {
    private TaskScheduler scheduler;
    private final List<SkyblockEntity> entities = new ArrayList<>();
    private final Pos[] positions;
    private final int tickFrequency;
    private final EntityConstructor constructor;
    private final Instance instance;

    public EntitySpawner(Pos[] positions, int tickFrequency, EntityConstructor constructor, Instance instance) {
        this.positions = positions;
        this.tickFrequency = tickFrequency;
        this.constructor = constructor;
        this.instance = instance;
        resume();
    }

    public void resume() {
        if (scheduler != null && scheduler.isRunning())
            throw new IllegalStateException("Already Running");
        for (Pos pos : positions) {
            SkyblockEntity entity = constructor.construct(pos, instance);
            entities.add(entity);
        }
        scheduler = new TaskScheduler() {
            @Override
            public void run() {
                respawnMissing();
            }
        };
        scheduler.repeatTask(tickFrequency, tickFrequency);
    }

    public void respawnMissing() {
        for (int i = 0; i < positions.length; i++) {
            if (!entities.get(i).isDead()) continue;
            SkyblockEntity entity = constructor.construct(positions[i], instance);
            entities.set(i, entity);
        }
    }

    public void stop() {
        scheduler.cancel();
    }

    public interface EntityConstructor {
        SkyblockEntity construct(Pos pos, Instance instance);
    }

    public record BasicConstructor(Supplier<SkyblockEntity> entityReturnable) implements EntityConstructor {
        @Override
        public SkyblockEntity construct(Pos pos, Instance instance) {
            SkyblockEntity entity = entityReturnable.get();
            entity.setInstance(instance, pos.add(0, 1.5, 0));
            return entity;
        }
    }
}
