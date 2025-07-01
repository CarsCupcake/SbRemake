package me.carscupcake.sbremake.entity.impl.hub;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.worlds.impl.Hub;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.attribute.Attribute;

public class GraveyardZombie extends SkyblockEntity implements SkillXpDropper {
    public GraveyardZombie() {
        super(EntityType.ZOMBIE);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.15f);
    }

    @Override
    public void spawn() {
        super.spawn();
        addAIGroup(zombieAiGroup(this, Hub.Region.Graveyard));
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

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 6;
    }
}
