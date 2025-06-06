package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.worlds.impl.SpidersDen;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SpiderJockey extends SkyblockEntity implements SkillXpDropper {
    private final boolean inSpidersMound;

    public SpiderJockey(boolean inSpidersMound) {
        super(EntityType.SPIDER);
        this.inSpidersMound = inSpidersMound;
    }

    @Override
    public void spawn() {
        addAIGroup(zombieAiGroup(this, inSpidersMound ? SpidersDen.Region.SpiderMound : SpidersDen.Region.ArachnesBurrow));
    }

    @Override
    public float getMaxHealth() {
        return inSpidersMound ? 220 : 1_000;
    }

    @Override
    public double getDamage() {
        return inSpidersMound ? 45 : 90;
    }

    @Override
    public String getName() {
        return "Spider Jockey";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return inSpidersMound ? 9 : 32;
    }

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos spawnPosition) {
        CompletableFuture<Void> future = super.setInstance(instance, spawnPosition);
        future.thenRun(() -> {
            SpiderJockeySkeleton skeleton = new SpiderJockeySkeleton(inSpidersMound);
            skeleton.setInstance(getInstance(), spawnPosition).thenRun(() -> SpiderJockey.this.addPassenger(skeleton));
        });
        return future;
    }

    @Override
    public int getLevel() {
        return inSpidersMound ? 3 : 42;
    }
}
