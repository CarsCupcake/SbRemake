package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.entity.EntityType;

import java.util.List;

public class SpiderJockeySilverfish extends SkyblockEntity implements SkillXpDropper {
    private final boolean inSpidersMound;

    public SpiderJockeySilverfish(boolean inSpidersMound) {
        super(EntityType.SILVERFISH, MobType.Arthropod);
        this.inSpidersMound = inSpidersMound;
    }

    @Override
    public void spawn() {
        addAIGroup(zombieAiGroup(this, inSpidersMound ? List.of(SpidersDen.Region.SpiderMound) : List.of(SpidersDen.Region.ArachnesBurrow, SpidersDen.Region.ArachnesSanctuary), true));
    }

    @Override
    public float getMaxHealth() {
        return 50;
    }

    @Override
    public String getName() {
        return "Silverfish";
    }

    @Override
    public int getLevel() {
        return inSpidersMound ? 3 : 42;
    }

    @Override
    public double getDamage() {
        return 20;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 5;
    }
}
