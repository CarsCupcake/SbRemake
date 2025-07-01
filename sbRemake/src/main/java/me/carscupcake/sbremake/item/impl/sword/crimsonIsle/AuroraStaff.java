package me.carscupcake.sbremake.item.impl.sword.crimsonIsle;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.ability.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.EntityUtils;
import me.carscupcake.sbremake.util.ParticleUtils;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuroraStaff implements ISbItem, ISbItem.StatProvider, NpcSellable, GemstoneSlots {
    private static final Map<SkyblockPlayer, RuneType> SELECTED_TYPES = new HashMap<>();
    private static final int RANGE = 30;
    private static final List<Ability> abilities = List.of(new ItemAbility<>("Swap Rune", AbilityType.LEFT_CLICK, event -> {
        var type = SELECTED_TYPES.getOrDefault(event.player(), RuneType.Mediator);
        SELECTED_TYPES.put(event.player(), switch (type) {
            case Mediator -> RuneType.Defender;
            case Defender -> RuneType.Virtuoso;
            case Virtuoso -> RuneType.Mediator;
        });
    }), new ItemAbility<>("Runic Zap", AbilityType.RIGHT_CLICK, event -> {
        var type = SELECTED_TYPES.getOrDefault(event.player(), RuneType.Mediator);
        final var eyePos = event.player().getPosition().add(0, event.player().getEyeHeight(), 0);
        var hit = EntityUtils.getEntitiesInLine(eyePos, eyePos.add(event.player().getPosition().direction().normalize().mul(RANGE)), event.player().getInstance());
        SkyblockEntity hitEntity = (SkyblockEntity) hit.stream().filter(entity -> entity instanceof SkyblockEntity).min((o1, o2) -> (int) (o1.getPosition().distance(eyePos) - o2.getPosition().distance(eyePos))).orElse(null);
        var steps = RANGE * 2;
        if (hitEntity != null) {
            steps = (int) (hitEntity.getDistance(eyePos) * 2);
            hitEntity.mageDamage(event.player(), 10_000, 0.2);
        }
        var dir = event.player().getPosition().direction().normalize().mul(0.5);
        var downVec = new Vec(0, -(event.player().getEyeHeight() / 3), 0);
        var pos = eyePos;
        for (int i = 0; i < steps; i++) {
            pos = pos.add(dir);
            ParticleUtils.spawnParticle(event.player().getInstance(), pos, type.getParticle(), 1);
            var down = pos.add(downVec);
            ParticleUtils.spawnParticle(event.player().getInstance(), down, type.getParticle(), 1);
            ParticleUtils.spawnParticle(event.player().getInstance(), down.add(downVec), type.getParticle(), 1);
        }
    }, new ManaRequirement<>(10), new CooldownRequirement<>(1)));
    private static final List<Requirement> REQUIREMENTS = List.of(new SkillRequirement(Skill.Combat, 22));

    @Override
    public String getId() {
        return "RUNIC_STAFF";
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }

    @Override
    public String getName() {
        return "Aurora Staff";
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 20, Stat.Intelligence, 250);
    }

    @Override
    public int sellPrice() {
        return 46_875;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    @Override
    public List<Requirement> requirements() {
        return REQUIREMENTS;
    }

    private static final GemstoneSlotType[] slots = {GemstoneSlotType.Sapphire, GemstoneSlotType.Sapphire};

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return slots;
    }

    private static final boolean[] unlocked = {false, false};

    @Override
    public boolean[] getUnlocked() {
        return unlocked;
    }

    private static final Cost[][] costs = {{new CoinsCost(250_000)}, {new CoinsCost(250_000)}};

    @Override
    public Cost[][] getLockedSlotCost() {
        return costs;
    }

    @Getter
    public enum RuneType {
        Mediator("§c§lMediator", Particle.FLAME), Defender("§3§lDefender", Particle.ENCHANTED_HIT), Virtuoso("§d§lVirtuoso", Particle.WITCH);

        private final String displayName;
        private final Particle particle;

        RuneType(String displayName, Particle particle) {
            this.displayName = displayName;
            this.particle = particle;
        }
    }
}
