package me.carscupcake.sbremake.entity.slayer.voidgloom;

import lombok.Getter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.SlayerEntity;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.PlayerDamageEntityEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.Characters;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.monster.EndermanMeta;

import java.util.function.Function;

public class VoidgloomSeraphI extends SlayerEntity implements SkillXpDropper {
    private int hits = getMaxHits();
    @Getter
    private int phase = 0;

    public VoidgloomSeraphI(SkyblockPlayer owner) {
        super(EntityType.ENDERMAN, new LootTable<>(), owner);
    }

    @Override
    public void spawn() {
        super.spawn();
        addAIGroup(slayerTarget());
        var task = new TaskScheduler() {

            @Override
            public void run() {
                if (owner.getPosition().distance(position) <= 6.5) {
                    owner.damage(getDissonanceDamage(), 0);
                }
            }
        };
        task.repeatTask(20, 20);
        assignTask(task);
        var meta = (EndermanMeta) getEntityMeta();
        meta.setScreaming(true);
        meta.setAggressive(true);
    }

    protected double getDissonanceDamage() {
        return getDamage() / 2;
    }

    public int getMaxHits() {
        return 15;
    }

    protected void onHitsLost() {
    }

    protected void onHitPhase() {
    }


    @Override
    public Function<SkyblockEntity, String> nameTag() {
        return (entity) -> {
            var voidgloom = (VoidgloomSeraphI) entity;
            if (voidgloom.hits == 0) return NameTagType.Slayer.apply(entity);
            var pers = ((double) voidgloom.getHits()) / ((double) voidgloom.getMaxHits());
            var color = pers > 0.6 ? "§r" : (pers > 0.3 ? "§d" : "§5");
            return "§c" + (Characters.Skull) + " §f" + (entity.getName()) + " " + color + "§l" + voidgloom.getHits() + " Hits";
        };
    }

    protected int getHits() {
        return hits;
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public ISlayer getSlayer() {
        return Slayers.Enderman;
    }

    @Override
    public float getMaxHealth() {
        return 300_000;
    }

    @Override
    public double getDamage() {
        return 1_200;
    }

    @Override
    public String getName() {
        return "§bVoidgloom Seraph";
    }

    @Override
    public void damage(double amount) {
        super.damage(amount);
    }

    @Override
    public boolean canTakeKnockback() {
        return false;
    }

    @Override
    protected float onDamage(SkyblockPlayer player, float amount) {
        if (hits != 0) {
            hits--;
            if (hits == 0) {
                phase++;
                onHitsLost();
            }
            return 1;
        }
        if (phase == 1 && getHealth() - amount <= getMaxHealth() * 0.66) {
            phase++;
            hits = getMaxHits();
            setHealth((float) (getMaxHealth() * 0.66));
            update();
            var event = new PlayerDamageEntityEvent(player, this, amount);
            spawnDamageTag(this, event.getDamageTag(amount));
            onHitPhase();
            return 0;
        }
        if (phase == 3 && getHealth() - amount <= getMaxHealth() * 0.33) {
            phase++;
            hits = getMaxHits();
            update();
            setHealth((float) (getMaxHealth() * 0.33));
            var event = new PlayerDamageEntityEvent(player, this, amount);
            spawnDamageTag(this, event.getDamageTag(amount));
            onHitPhase();
            return 0;
        }
        return amount;
    }

    @Override
    public void doFerocity(SkyblockPlayer player, double ferocity) {
        super.doFerocity(player, hits == 0 ? (ferocity * 0.25) : ferocity);
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 100;
    }
}
