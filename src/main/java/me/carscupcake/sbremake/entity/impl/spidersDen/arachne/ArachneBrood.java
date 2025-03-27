package me.carscupcake.sbremake.entity.impl.spidersDen.arachne;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.entity.EntityType;

public class ArachneBrood extends SkyblockEntity {
    private final Arachne parent;
    private final int level;
    private final float maxHealth;
    private final double damage;
    public ArachneBrood(Arachne parent) {
        super(EntityType.CAVE_SPIDER);
        this.parent = parent;
        this.level = parent.isCristal() ? 200 : 100;
        this.maxHealth = parent.isCristal() ? 20_000 : 4_000;
        this.damage = parent.isCristal() ? 400 : 200;
        addAIGroup(zombieAiGroup(this, SpidersDen.Region.ArachnesSanctuary));
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
        return "Arachne's Brood";
    }

    @Override
    public void kill() {
        super.kill();
        parent.killMini(this);
    }
}
