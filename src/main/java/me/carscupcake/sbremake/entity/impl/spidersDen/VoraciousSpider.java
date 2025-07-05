package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.entity.EntityType;

public class VoraciousSpider extends SkyblockEntity {
    private final float maxHealth;
    private final int level;
    private final double damage;
    private final int tier;

    public VoraciousSpider(int tier) {
        super(EntityType.SPIDER);
        this.tier = tier;
        maxHealth = switch (tier) {
            case 1 -> 300;
            case 2 -> 750;
            case 3 -> 1_150;
            case 4 -> 1_450;
            default -> throw new IllegalStateException("Not allowed");
        };
        level = switch (tier) {
            case 1 -> 10;
            case 2 -> 42;
            case 3 -> 45;
            case 4 -> 50;
            default -> throw new IllegalStateException("Not allowed");
        };
        damage = switch (tier) {
            case 1 -> 80;
            case 2 -> 95;
            case 3 -> 150;
            case 4 -> 170;
            default -> throw new IllegalStateException();
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
        return "Voracious Spider";
    }

    @Override
    public double getDamage() {
        return damage;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
