package me.carscupcake.sbremake.entity.slayer.voidgloom.miniboss;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import net.minestom.server.entity.EntityType;

public class VoidcrazedManiac extends SkyblockEntity implements SkillXpDropper {
    public VoidcrazedManiac() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 52_500_000;
    }

    @Override
    public String getName() {
        return "§4Voidcrazed Maniac";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 5_000;
    }

    @Override
    public double getDamage() {
        return 25_000;
    }

    @Override
    public int getLevel() {
        return 4_330;
    }
}
