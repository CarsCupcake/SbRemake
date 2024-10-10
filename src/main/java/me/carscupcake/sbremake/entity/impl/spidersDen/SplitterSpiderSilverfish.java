package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.Range;

import java.util.List;

public class SplitterSpiderSilverfish extends SkyblockEntity implements SkillXpDropper {
    private final int level;
    public SplitterSpiderSilverfish(@Range(from = 1, to = 4) int tier) {
        super(EntityType.SILVERFISH);
        level = switch (tier) {
            case 1 -> 2;
            case 2 -> 42;
            case 3 -> 45;
            case 4 -> 50;
            default -> throw new IllegalStateException("Not in range");
        };
        addAIGroup(zombieAiGroup(this, tier == 1 ? List.of(SpidersDen.Region.SpiderMound) : List.of(SpidersDen.Region.ArachnesBurrow, SpidersDen.Region.ArachnesSanctuary)));
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
        return level;
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
