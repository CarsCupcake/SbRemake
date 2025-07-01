package me.carscupcake.sbremake.entity.impl.deepCaverns;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.goal.CreeperGoal;
import me.carscupcake.sbremake.worlds.impl.DeepCaverns;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.ai.EntityAIGroup;

public class SneakyCreeper extends SkyblockEntity {
    private CreeperGoal goal;

    public SneakyCreeper() {
        super(EntityType.CREEPER);
        setInvisible(true);
    }

    @Override
    public void spawn() {
        EntityAIGroup group = regionTarget(this, DeepCaverns.Region.GunpowderMines, 8);
        goal = new CreeperGoal(this, 3, 1);
        group.getGoalSelectors().add(goal);
        addAIGroup(group);
    }

    @Override
    public float getMaxHealth() {
        return 120;
    }

    @Override
    public double getDamage() {
        return 80;
    }

    @Override
    public String getName() {
        return "Sneaky Creeper";
    }

    @Override
    public void tick(long time) {
        super.tick(time);
        if (isInvisible() && !goal.isStop())
            setInvisible(false);
    }

    @Override
    public int getLevel() {
        return 3;
    }
}
