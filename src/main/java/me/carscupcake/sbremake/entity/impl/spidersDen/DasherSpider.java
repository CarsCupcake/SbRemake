package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.MeleeAttackGoalEntity;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DasherSpider extends SkyblockEntity implements MeleeAttackGoalEntity {
    private final float maxHealth;
    private final double damage;
    private final int level;

    public DasherSpider(int tier) {
        super(EntityType.SPIDER);
        maxHealth = switch (tier) {
            case 1 -> 170;
            case 2 -> 900;
            case 3 -> 1_100;
            case 4 -> 1_400;
            default -> throw new IllegalStateException("Not allowed");
        };
        level = switch (tier) {
            case 1 -> 4;
            case 2 -> 42;
            case 3 -> 45;
            case 4 -> 50;
            default -> throw new IllegalStateException("Not allowed");
        };
        damage = switch (tier) {
            case 1 -> 55;
            case 2 -> 90;
            case 3 -> 110;
            case 4 -> 150;
            default -> throw new IllegalStateException();
        };
        addAIGroup(zombieAiGroup(this, tier == 1 ? SpidersDen.Region.SpiderMound : SpidersDen.Region.ArachnesBurrow));
    }

    @Override
    public float getMaxHealth() {
        return maxHealth;
    }

    @Override
    public String getName() {
        return "Dasher Spider";
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public int getLevel() {
        return level;
    }

    private TaskScheduler jumpTask;
    private Entity cashed = null;

    @Override
    public void setGoalTarget(@Nullable Entity target) {
        if (target != cashed && target != null) {
            cashed = target;
            if (jumpTask != null && jumpTask.isRunning()) {
                jumpTask.cancel();
            }
            jumpTask = new TaskScheduler() {
                private int jumpCounter = new Random().nextInt(2) + 2;

                @Override
                public void run() {
                    jumpCounter--;
                    Pos targetPos = target.getPosition();
                    Vec dir = targetPos.asVec().sub(getPosition()).normalize();
                    setVelocity(dir);
                    if (jumpCounter == 0) {
                        cancel();
                    }
                }
            };
            jumpTask.repeatTask(40, 40);
        } else if (target == null && cashed != null) {
            cashed = null;
        }
    }

    @Override
    public void kill() {
        if (jumpTask != null && jumpTask.isRunning()) jumpTask.cancel();
        super.kill();
    }
}
