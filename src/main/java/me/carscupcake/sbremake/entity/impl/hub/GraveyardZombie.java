package me.carscupcake.sbremake.entity.impl.hub;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.attribute.Attribute;
import net.minestom.server.entity.EntityType;

import java.util.List;

public class GraveyardZombie extends SkyblockEntity {
    public GraveyardZombie() {
        super(EntityType.ZOMBIE);
        addAIGroup(zombieAiGroup(this));
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.23f);
    }

    @Override
    public float getMaxHealth() {
        return 100;
    }

    @Override
    public String getName() {
        return "Graveyard Zombie";
    }

    @Override
    public double getDamage() {
        return 20;
    }

    @Override
    public int getLevel() {
        return 1;
    }
}
