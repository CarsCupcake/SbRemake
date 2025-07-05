package me.carscupcake.sbremake.entity.impl.spidersDen.arachne;

import lombok.Getter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

public class Arachne extends SkyblockEntity {
    @Getter
    private final boolean cristal;
    private final float maxHealth;
    private final int level;
    private final double damage;

    public Arachne(boolean cristal) {
        super(EntityType.SPIDER);
        this.cristal = cristal;
        level = cristal ? 500 : 300;
        maxHealth = cristal ? 100_000 : 20_000;
        damage = cristal ? 600 : 300;
        threashhold = maxHealth * 0.5f;
    }

    @Override
    public void spawn() {
        addAIGroup(zombieAiGroup(this, SpidersDen.Region.ArachnesSanctuary, true));
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
        return "Arachne";
    }

    private int stage = 0;
    private float threashhold;
    private final Set<ArachneBrood> arachneBroods = new HashSet<>();

    @Override
    protected float onDamage(SkyblockPlayer player, float amount) {
        amount = Math.min(amount, 1_000);
        if (getHealth() - amount <= threashhold && stage < 2) {
            stage++;
            for (int i = 0; i < 10; i++) {
                ArachneBrood arachneBrood = new ArachneBrood(this);
                arachneBroods.add(arachneBrood);
                arachneBrood.setInstance(instance, position);
            }
            remove(false);
        }
        return amount;
    }

    public void killMini(ArachneBrood arachneBrood) {
        arachneBroods.remove(arachneBrood);
        if (arachneBroods.size() == 1) {
            arachneBrood = arachneBroods.iterator().next();
            var health = getHealth();
            setInstance(arachneBrood.getInstance(), arachneBrood.getPosition());
            setHealth(health);
            arachneBrood.remove();
            if (stage == 1) {
                threashhold = maxHealth * 0.2f;
            }
        }
    }
}
