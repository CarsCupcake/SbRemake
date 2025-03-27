package me.carscupcake.sbremake.entity.goal;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.utils.position.PositionUtils;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Random;

public class SlimeAttackGoal extends GoalSelector {
    private long lastHit;
    private final double range;
    private final Duration delay;
    private boolean stop;
    private Entity cachedTarget;
    private final SkyblockEntity entity;
    private final double jumpHeight;
    private Cooldown jumpCooldown;
    private final Random random = new Random();
    private boolean jumped = false;

    public SlimeAttackGoal(@NotNull SkyblockEntity entity, Duration delay, double jumpHeight) {
        super(entity);
        this.entity = entity;
        this.range = entity.getBoundingBox().maxZ() * 2;
        this.delay = delay;
        this.jumpCooldown = new Cooldown(Duration.of(random.nextInt(20) + 10, TimeUnit.SERVER_TICK));
        this.jumpHeight = jumpHeight;
    }

    public boolean shouldStart() {
        this.cachedTarget = this.findTarget();
        return this.cachedTarget != null;
    }

    public void start() {
    }

    public void tick(long time) {
        Entity target;
        if (this.cachedTarget != null) {
            target = this.cachedTarget;
            this.cachedTarget = null;
        } else {
            target = this.findTarget();
        }
        this.stop = target == null;
        if (!this.stop && entity.isOnGround()) {
            if (this.entityCreature.getDistanceSquared(target) <= this.range * this.range) {
                this.entityCreature.lookAt(target);
                if (!Cooldown.hasCooldown(time, this.lastHit, this.delay)) {
                    this.entityCreature.attack(target, false);
                    this.lastHit = time;
                    entity.setVelocity(new Vec(0, jumpHeight, 0));
                }
                return;
            }

            if (jumped) {
                jumped = false;
                jumpCooldown = new Cooldown(Duration.of(random.nextInt(20) + 10, TimeUnit.SERVER_TICK));
                return;
            }

            if (jumpCooldown.isReady(time)) {
                Vec v = target.getPosition().asVec().sub(entity.getPosition()).withY(0).normalize().mul(2).withY(jumpHeight);
                float yaw = PositionUtils.getLookYaw(v.x(), v.z());
                float pitch = PositionUtils.getLookPitch(v.x(), 0, v.z());
                entity.teleport(entity.getPosition().withYaw(yaw).withPitch(pitch));
                entity.setVelocity(v);
                jumped = true;
            }
        }

    }

    public boolean shouldEnd() {
        return this.stop;
    }

    public void end() {
    }
}
