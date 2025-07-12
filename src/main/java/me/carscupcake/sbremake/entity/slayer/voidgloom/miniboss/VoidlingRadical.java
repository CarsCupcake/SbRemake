package me.carscupcake.sbremake.entity.slayer.voidgloom.miniboss;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import net.minestom.server.entity.EntityType;

public class VoidlingRadical extends SkyblockEntity implements SkillXpDropper {
    public VoidlingRadical() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 17_500_000;
    }

    @Override
    public String getName() {
        return "§5Voidling Radical";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 2_000;
    }

    @Override
    public double getDamage() {
        return 7_400;
    }

    @Override
    public int getLevel() {
        return 2_500;
    }
}
