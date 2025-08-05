package me.carscupcake.sbremake.entity.impl.end;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import net.minestom.server.entity.EntityType;

public class Endermite extends SkyblockEntity implements SkillXpDropper {
    private final float maxHealth;
    private final double damage;
    private final int level;

    public Endermite(boolean hard) {
        super(EntityType.ENDERMITE, MobType.Ender);
        maxHealth = (hard) ? 2_300 : 2_000;
        damage = (hard) ? 475 : 400;
        level = (hard) ? 40 : 37;
        update();
    }

    @Override
    public void spawn() {
        addAIGroup(zombieAiGroup(this, true));
    }

    @Override
    public float getMaxHealth() {
        return maxHealth;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public String getName() {
        return "Endermite";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 40;
    }
}
