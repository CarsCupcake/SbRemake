package me.carscupcake.sbremake.entity.goal;

import lombok.Getter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.SoundType;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.metadata.monster.CreeperMeta;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.network.packet.server.play.ExplosionPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
public class CreeperGoal extends GoalSelector {
    private final SkyblockEntity entity;
    private final float explosionRange;
    private final double desiredRange;
    private Entity cachedTarget;
    private boolean stop = true;
    private int explosionTicks = 30;

    public CreeperGoal(@NotNull SkyblockEntity entityCreature, float explosionRange, double desiredRange) {
        super(entityCreature);
        this.entity = entityCreature;
        this.explosionRange = explosionRange;
        this.desiredRange = desiredRange;
    }

    @Override
    public boolean shouldStart() {
        cachedTarget = this.findTarget();
        return !entity.isDead() && cachedTarget != null;
    }

    @Override
    public void start() {
        entity.getNavigator().setPathTo(cachedTarget.getPosition());
    }

    @Override
    public void tick(long l) {
        Entity target;
        if (cachedTarget != null) {
            target = cachedTarget;
            cachedTarget = null;
        } else target = findTarget();
        stop = target == null;
        if (!stop) {
            double range = entity.getPosition().distance(Objects.requireNonNull(target).getPosition());
            if (range < desiredRange || (range < explosionRange && explosionTicks < 30)) {
                if (explosionTicks == 0) {
                    stop = true;
                    ExplosionPacket packet = new ExplosionPacket(entity.getPosition().x(), entity.getPosition().y(), entity.getPosition().z(), explosionRange, new byte[0], 0, 0, 0);
                    entity.getInstance().sendGroupedPacket(packet);
                    for (Entity e : entity.getInstance().getNearbyEntities(entity.getPosition(), explosionRange)) {
                        if (e instanceof SkyblockPlayer player) {
                            player.damage(entity);
                        }
                    }
                    entity.remove();
                    return;
                }
                if (explosionTicks == 30 && entity.getEntityType() == EntityType.CREEPER) {
                    entity.editEntityMeta(CreeperMeta.class, meta -> meta.setIgnited(true));
                    entity.getInstance().playSound(SoundType.ENTITY_CREEPER_PRIMED.create(1f, 1f), entity.getPosition());
                }
                explosionTicks--;
            } else {
                if (explosionTicks != 30) {
                    explosionTicks = 30;
                    if (entity.getEntityType() == EntityType.CREEPER)
                        entity.editEntityMeta(CreeperMeta.class, meta -> meta.setIgnited(false));
                }
                Navigator navigator = this.entityCreature.getNavigator();
                Point pathPosition = navigator.getPathPosition();
                Pos targetPosition = target.getPosition();
                if ((pathPosition == null || !pathPosition.samePoint(targetPosition))) {
                    navigator.setPathTo(targetPosition);
                }
            }
        }
    }

    @Override
    public boolean shouldEnd() {
        return stop;
    }

    @Override
    public void end() {
        explosionTicks = 30;
        this.entityCreature.getNavigator().setPathTo((Point) null);
    }
}
