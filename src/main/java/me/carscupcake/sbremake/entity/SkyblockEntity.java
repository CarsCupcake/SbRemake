package me.carscupcake.sbremake.entity;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerProjectileDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerToEntityDamageEvent;
import me.carscupcake.sbremake.event.PlayerToEntityMageDamage;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.SkyblockPlayerArrow;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.goal.MeleeAttackGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.goal.RangedAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

public abstract class SkyblockEntity extends EntityCreature {
    public SkyblockEntity(@NotNull EntityType entityType) {
        super(entityType, UUID.randomUUID());
        setHealth(getMaxHealth());
    }

    @Override
    public abstract float getMaxHealth();

    public double getDamage() {
        return 0;
    }

    public double getTrueDamage() {
        return 0;
    }

    public float getDefense() {
        return 0;
    }

    public boolean canTakeKnockback() {
        return true;
    }

    public Function<SkyblockEntity, String> nameTag() {
        return NameTagType.Basic;
    }

    public int getLevel() {
        return -1;
    }

    public double abilityDamageMultiplier() {
        return 1;
    }

    public abstract String getName();

    public void damage(SkyblockPlayer player) {
        PlayerMeleeDamageEntityEvent event = new PlayerMeleeDamageEntityEvent(player, this, player.getStat(Stat.Damage), player.getStat(Stat.Strength), player.getStat(Stat.CritDamage), player.getStat(Stat.CritChance));
        EventDispatcher.call(event);
        damage(event);
    }

    public void mageDamage(SkyblockPlayer player, double baseAbilityDamage, double abilityScaling) {
        PlayerToEntityMageDamage event = new PlayerToEntityMageDamage(player, this, baseAbilityDamage, player.getStat(Stat.Intelligence), abilityScaling);
        EventDispatcher.call(event);
        if (event.isCancelled()) return;
        float damage = (float) (event.calculateHit() * abilityDamageMultiplier());
        damage = onDamage(damage);
        if (damage <= 0) return;
        spawnDamageTag(this, event.getDamageTag());
        damage(DamageType.PLAYER_ATTACK, damage * (1 - (getDefense() / (getDefense() + 100))));
    }

    public void damage(SkyblockPlayerArrow projectile) {
        PlayerProjectileDamageEntityEvent event = new PlayerProjectileDamageEntityEvent(this, projectile);
        EventDispatcher.call(event);
        damage(event);
    }

    public void damage(PlayerToEntityDamageEvent event) {
        if (event.isCancelled()) return;
        float damage = (float) ((event.isCrit()) ? event.calculateCritHit() : event.calculateHit());
        damage = damage * (1 - (getDefense() / (getDefense() + 100)));
        damage = onDamage(damage);
        if (damage <= 0) return;
        spawnDamageTag(this, event.getDamageTag());
        damage(DamageType.PLAYER_ATTACK, damage * (1 - (getDefense() / (getDefense() + 100))));
        if (canTakeKnockback())
            this.takeKnockback(0.4f, Math.sin(event.damagerPos().yaw() * 0.017453292), -Math.cos(event.damagerPos().yaw() * 0.017453292));
    }

    public static void spawnDamageTag(SkyblockEntity entity, String tag) {
        EntityCreature creature = new EntityCreature(EntityType.ARMOR_STAND, UUID.randomUUID());
        creature.setCustomName(Component.text(tag));
        creature.setCustomNameVisible(true);
        creature.setInvisible(true);
        creature.setNoGravity(true);
        creature.setInvulnerable(true);
        ArmorStandMeta meta = (ArmorStandMeta) creature.getEntityMeta();
        meta.setHasNoBasePlate(true);
        meta.setMarker(true);
        BoundingBox bb = entity.getBoundingBox();
        int random = new Random().nextInt(4);
        double x = switch (random) {
            case 0 -> bb.maxX() + 0.5;
            case 2 -> bb.minX() - 0.5;
            default -> 0;
        };
        double z = switch (random) {
            case 1 -> bb.maxZ() + 0.5;
            case 3 -> bb.minZ() - 0.5;
            default -> 0;
        };
        Pos pos = entity.getPosition().add(x, 0.7d, z);
        creature.setInstance(entity.getInstance(), pos.add(0, new Random().nextDouble(0.5) - 0.25, 0));
        creature.scheduler().buildTask(creature::remove).delay(Duration.ofSeconds(1)).schedule();
    }

    public void damage(double amount) {
        damage(DamageType.GENERIC, (float) amount);
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        update();
    }

    @Override
    public float getHealth() {
        return super.getHealth();
    }

    public void update() {
        if (isDead) return;
        String name = nameTag().apply(this);
        setCustomName(Component.text(name));
        setCustomNameVisible(true);
    }

    /**
     * Is called to modifier and execute on damage
     *
     * @param amount the calcualted damaege amount
     * @return the new damage amount
     */
    protected float onDamage(float amount) {
        return amount;
    }

    public enum NameTagType implements Function<SkyblockEntity, String> {
        Basic() {
            @Override
            public String apply(SkyblockEntity skyblockEntity) {
                return STR."§8[§7Lv\{skyblockEntity.getLevel()}§8] §c\{skyblockEntity.getName()} §a\{StringUtils.cleanDouble(skyblockEntity.getHealth(), 0)}§7/§a\{StringUtils.cleanDouble(skyblockEntity.getMaxHealth())}§c\{Stat.Health.getSymbol()}";
            }
        }
    }

    public static void init() {

    }

    protected static EntityAIGroup zombieAiGroup(SkyblockEntity entity) {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().addAll(List.of(new MeleeAttackGoal(entity, 1.6, 20, TimeUnit.SERVER_TICK), new RandomStrollGoal(entity, 20) // Walk around
        ));
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(entity, 32), new ClosestEntityTarget(entity, 32, entity1 -> entity1 instanceof Player)));
        return aiGroup;
    }

    protected static RangedAttackGoal createRangedAttackGoal(SkyblockEntity entity) {
        RangedAttackGoal rangedAttackGoal = new RangedAttackGoal(entity, 80, 25, 15, false, 1, 0.2, TimeUnit.SERVER_TICK);
        rangedAttackGoal.setProjectileGenerator(_ -> new SkyblockEntityProjectile(entity, EntityType.ARROW));
        return rangedAttackGoal;
    }

    protected static EntityAIGroup skeletonAiGroup(SkyblockEntity entity) {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().addAll(List.of(createRangedAttackGoal(entity), new RandomStrollGoal(entity, 5) // Walk around
        ));
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(entity, 32), new ClosestEntityTarget(entity, 32, entity1 -> entity1 instanceof Player)));
        return aiGroup;
    }
}
