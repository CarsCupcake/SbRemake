package me.carscupcake.sbremake.entity.impl.spidersDen;

import lombok.Getter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.TargetSelector;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityAnimationPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

public class SpiderJockeySkeleton extends SkyblockEntity implements SkillXpDropper {
    private final boolean inSpidersMound;
    public SpiderJockeySkeleton(boolean inSpidersMound) {
        super(EntityType.SKELETON);
        this.inSpidersMound = inSpidersMound;
        setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(Material.BOW));
        RangedSilverfishAttackGoal rangedAttackGoal = new RangedSilverfishAttackGoal(this, 40, 25, 15, false, TimeUnit.SERVER_TICK);
        EntityAIGroup group = new EntityAIGroup();
        group.getGoalSelectors().add(rangedAttackGoal);
        group.getTargetSelectors().addAll(regionTarget(this, inSpidersMound ? SpidersDen.Region.SpiderMound : SpidersDen.Region.ArachnesBurrow, 16).getTargetSelectors());
        addAIGroup(group);
    }

    @Override
    public float getMaxHealth() {
        return inSpidersMound ? 220 : 1_000;
    }

    @Override
    public String getName() {
        return "Jockey Skeleton";
    }

    @Override
    public double getDamage() {
        return inSpidersMound ? 40 : 90;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return inSpidersMound ? 9 : 32;
    }

    @Override
    public int getLevel() {
        return inSpidersMound ? 3 : 42;
    }
    public static class RangedSilverfishAttackGoal extends GoalSelector {
        @Getter
        private final Cooldown cooldown = new Cooldown(Duration.of(5, TimeUnit.SERVER_TICK));

        private long lastShot;
        private final Duration delay;
        private final int attackRangeSquared;
        private final int desirableRangeSquared;
        private final boolean comeClose;

        private boolean stop;
        private Entity cachedTarget;

        public RangedSilverfishAttackGoal(@NotNull EntityCreature entityCreature, int delay, int attackRange, int desirableRange, boolean comeClose, @NotNull TemporalUnit timeUnit) {
            this(entityCreature, Duration.of(delay, timeUnit), attackRange, desirableRange, comeClose);
        }

        public RangedSilverfishAttackGoal(@NotNull EntityCreature entityCreature, Duration delay, int attackRange, int desirableRange, boolean comeClose) {
            super(entityCreature);
            this.delay = delay;
            this.attackRangeSquared = attackRange * attackRange;
            this.desirableRangeSquared = desirableRange * desirableRange;
            this.comeClose = comeClose;
            Check.argCondition(desirableRange > attackRange, "Desirable range can not exceed attack range!");
        }

        @Override
        public boolean shouldStart() {
            this.cachedTarget = findTarget();
            return this.cachedTarget != null;
        }

        @Override
        public void start() {
            this.entityCreature.getNavigator().setPathTo(this.cachedTarget.getPosition());
        }
        private boolean isPulling = false;
        @Override
        public void tick(long time) {
            Entity target;
            if (this.cachedTarget != null) {
                target = this.cachedTarget;
                this.cachedTarget = null;
            } else {
                target = findTarget();
            }
            if (target == null) {
                this.stop = true;
                return;
            }
            double distanceSquared = this.entityCreature.getDistanceSquared(target);
            boolean comeClose = false;
            if (distanceSquared <= this.attackRangeSquared) {
                if (!Cooldown.hasCooldown(time, this.lastShot, this.delay)) {
                    if (this.entityCreature.hasLineOfSight(target)) {
                        if (isPulling) {
                            for (Player player : entityCreature.getViewers())
                                player.sendPacket(new EntityMetaDataPacket(entityCreature.getEntityId(), Map.of(8, Metadata.Byte((byte) 0))));
                            final var to = target.getPosition().add(0D, target.getEyeHeight(), 0D);
                            Vec dir = to.sub(entityCreature.getPosition().add(0, entityCreature.getEyeHeight(), 0)).asVec().withY(0).normalize().mul(0.3);
                            SpiderJockeySilverfish spiderJockeySilverfish = new SpiderJockeySilverfish(((SpiderJockeySkeleton) entityCreature).inSpidersMound);
                            spiderJockeySilverfish.setInstance(entityCreature.getInstance(), entityCreature.getPosition().add(0, entityCreature.getEyeHeight(), 0))
                                    .thenRun(() -> {
                                        spiderJockeySilverfish.setVelocity(dir);
                                    });
                        } else {
                            for (Player player : entityCreature.getViewers())
                                player.sendPacket(new EntityMetaDataPacket(entityCreature.getEntityId(), Map.of(8, Metadata.Byte((byte) 1))));
                        }
                        this.lastShot = time;
                    } else {
                        comeClose = this.comeClose;
                    }
                }
            }
            Navigator navigator = this.entityCreature.getNavigator();
            final var pathPosition = navigator.getPathPosition();
            if (!comeClose && distanceSquared <= this.desirableRangeSquared) {
                if (pathPosition != null) {
                    navigator.setPathTo(null);
                }
                this.entityCreature.lookAt(target);
                return;
            }
            final var targetPosition = target.getPosition();
            if (pathPosition == null || !pathPosition.samePoint(targetPosition)) {
                if (this.cooldown.isReady(time)) {
                    this.cooldown.refreshLastUpdate(time);
                    navigator.setPathTo(targetPosition);
                }
            }
        }

        @Override
        public boolean shouldEnd() {
            return this.stop;
        }

        @Override
        public void end() {
            // Stop following the target
            this.entityCreature.getNavigator().setPathTo(null);
        }
    }
}
