package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.Range;

public class SplitterSpider extends SkyblockEntity implements SkillXpDropper {
    private final int level;
    private final float maxHealth;
    private final double damage;
    private final double combatXp;
    private final int tier;

    public SplitterSpider(@Range(from = 1, to = 4) int tier) {
        super(EntityType.SPIDER, MobType.Arthropod);
        this.tier = tier;
        level = switch (tier) {
            case 1 -> 2;
            case 2 -> 42;
            case 3 -> 45;
            case 4 -> 50;
            default -> throw new IllegalStateException("Not in range");
        };
        damage = switch (tier) {
            case 1 -> 30;
            case 2 -> 90;
            case 3 -> 120;
            case 4 -> 160;
            default -> throw new IllegalStateException("Not in range");
        };
        maxHealth = switch (tier) {
            case 1 -> 180;
            case 2 -> 800;
            case 3 -> 1_100;
            case 4 -> 1_450;
            default -> throw new IllegalStateException("Not in range");
        };
        combatXp = switch (tier) {
            case 1 -> 9;
            case 2 -> 28;
            case 3 -> 30;
            case 4 -> 36;
            default -> throw new IllegalStateException("Not in range");
        };
    }

    @Override
    public void spawn() {
        addAIGroup(zombieAiGroup(this, tier == 1 ? SpidersDen.Region.SpiderMound : SpidersDen.Region.ArachnesBurrow, true));
    }

    @Override
    public float getMaxHealth() {
        return maxHealth;
    }

    @Override
    public String getName() {
        return "Splitter Spider";
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return combatXp;
    }

    @Override
    public void kill() {
        super.kill();
        SplitterSpiderSilverfish splitterSpiderSilverfish = new SplitterSpiderSilverfish(tier);
        splitterSpiderSilverfish.setInstance(getInstance(), getPosition().add(0.5, 0, 0.5));
        splitterSpiderSilverfish = new SplitterSpiderSilverfish(tier);
        splitterSpiderSilverfish.setInstance(getInstance(), getPosition().sub(0.5, 0, 0.5));
    }
}
