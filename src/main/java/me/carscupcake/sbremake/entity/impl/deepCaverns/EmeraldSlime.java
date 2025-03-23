package me.carscupcake.sbremake.entity.impl.deepCaverns;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.goal.SlimeAttackGoal;
import me.carscupcake.sbremake.worlds.impl.DeepCaverns;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.metadata.other.SlimeMeta;
import net.minestom.server.utils.time.TimeUnit;

import java.time.Duration;

public class EmeraldSlime extends SkyblockEntity {
    private final int level;

    public EmeraldSlime(int level) {
        super(EntityType.SLIME);
        this.level = level;
        setHealth(getMaxHealth());
        editEntityMeta(SlimeMeta.class, slimeMeta -> slimeMeta.setSize(switch (level) {
            case 5 -> 3;
            case 10 -> 6;
            default -> 11;
        }));
        EntityAIGroup group = regionTarget(this, DeepCaverns.Region.Slimehill, 16);
        group.getGoalSelectors().add(new SlimeAttackGoal(this, Duration.of(10, TimeUnit.SERVER_TICK), 10));
        addAIGroup(group);
    }

    @Override
    public float getMaxHealth() {
        return switch (level) {
            case 5 -> 80;
            case 10 -> 150;
            default -> 250;
        };
    }

    @Override
    public double getDamage() {
        return switch (level) {
            case 5 -> 70;
            case 10 -> 100;
            default -> 150;
        };
    }

    @Override
    public String getName() {
        return "Emerald Slime";
    }

    @Override
    public int getLevel() {
        return level;
    }
}
