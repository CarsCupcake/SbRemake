package me.carscupcake.sbremake.entity.slayer.zombie;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class RevenantAttackGoal extends GoalSelector {

    private final Cooldown cooldown = new Cooldown(Duration.of(5, TimeUnit.SERVER_TICK));

    private long lastHit;
    private final double range;
    private final double longTimeRange;
    private final Duration delay;
    private final Duration longTime;

    private boolean stop;
    private Entity cachedTarget;

    /**
     * @param entityCreature the entity to add the goal to
     * @param range          the allowed range the entity can attack others.
     * @param delay          the delay between each attacks
     */
    public RevenantAttackGoal(@NotNull EntityCreature entityCreature, double range, double longTimeRange, Duration longTime, Duration delay) {
        super(entityCreature);
        this.range = range;
        this.delay = delay;
        this.longTimeRange = longTimeRange;
        this.longTime = longTime;
    }

    public @NotNull Cooldown getCooldown() {
        return this.cooldown;
    }

    @Override
    public boolean shouldStart() {
        this.cachedTarget = findTarget();
        return this.cachedTarget != null;
    }

    @Override
    public void start() {
        final Point targetPosition = this.cachedTarget.getPosition();
        entityCreature.getNavigator().setPathTo(targetPosition);
    }

    @Override
    public void tick(long time) {
        Entity target;
        if (this.cachedTarget != null) {
            target = this.cachedTarget;
            this.cachedTarget = null;
        } else {
            target = findTarget();
        }

        this.stop = target == null;

        if (!stop) {

            // Attack the target entity
            double targetedRange = Cooldown.hasCooldown(time, lastHit, longTime) ? (longTimeRange * longTimeRange) : (range * range);
            if (entityCreature instanceof RevenantHorrorIII revenantHorrorIII && revenantHorrorIII.isEnraged()) {
                targetedRange += 1.5;
            }
            if (entityCreature.getDistanceSquared(target) <= targetedRange) {
                entityCreature.lookAt(target);
                if (!Cooldown.hasCooldown(time, lastHit, delay)) {
                    entityCreature.attack(target, true);
                    this.lastHit = time;
                }
                return;
            }

            // Move toward the target entity
            SkyblockEntity.MeleeAttackGoal.navigator(time, target, entityCreature.getNavigator(), this.cooldown);
        }
    }

    @Override
    public boolean shouldEnd() {
        return stop;
    }

    @Override
    public void end() {
        // Stop following the target
        entityCreature.getNavigator().setPathTo(null);
    }
}
