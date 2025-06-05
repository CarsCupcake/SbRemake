package me.carscupcake.sbremake.entity.goal;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.SkyblockEntityProjectile;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.metadata.monster.BlazeMeta;
import org.jetbrains.annotations.NotNull;

public class BlazeGoal extends GoalSelector {

    private final SkyblockEntity entity;
    private int attackCooldown = 40;
    private final BlazeMeta blazeMeta;
    private final double distance;

    public BlazeGoal(@NotNull SkyblockEntity entityCreature, double distance) {
        super(entityCreature);
        this.entity = entityCreature;
        blazeMeta = (entity.getEntityMeta() instanceof BlazeMeta) ? (BlazeMeta) entity.getEntityMeta() : null;
        this.distance = distance;
    }

    @Override
    public boolean shouldStart() {
        return entity.getTarget() != null && !entity.getTarget().isRemoved() && entity.getDistance(entity.getTarget()) <= distance && entity.hasLineOfSight(entity.getTarget(), false);
    }

    @Override
    public void start() {
        attackCooldown = 40;
        entity.getNavigator().setPathTo(entity.getPosition().add(0, 3, 0));
    }

    @Override
    public void tick(long time) {
        --attackCooldown;
        if (attackCooldown == 20 && blazeMeta != null) {
            blazeMeta.setOnFire(true);
        }
        entity.lookAt(entity.getTarget());
        if (attackCooldown == 0) {
            attackCooldown = 40;
            if (blazeMeta != null) {
                blazeMeta.setOnFire(false);
            }
            var entityProjectile = new SkyblockEntityProjectile(entity, EntityType.SMALL_FIREBALL, false);
        }
    }

    @Override
    public boolean shouldEnd() {
        return entity.getTarget() == null || entity.getTarget().isRemoved() || !entity.hasLineOfSight(entity.getTarget());
    }

    @Override
    public void end() {
        entity.getNavigator().setPathTo(entity.getPosition().add(0, -3, 0));
    }
}
