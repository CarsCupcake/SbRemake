package me.carscupcake.sbremake.entity.slayer.voidgloom.miniboss;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import net.minestom.server.entity.EntityType;

public class VoidlingDevotee extends SkyblockEntity implements SkillXpDropper {
    public VoidlingDevotee() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 8_400_000;
    }

    @Override
    public String getName() {
        return "§5Voidling Devotee";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 800;
    }

    @Override
    public double getDamage() {
        return 5_000;
    }

    @Override
    public int getLevel() {
        return 1_730;
    }
}
