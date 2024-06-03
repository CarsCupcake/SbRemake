package me.carscupcake.sbremake.entity;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.event.EventDispatcher;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;

public abstract class SkyblockEntity extends EntityCreature {
    public SkyblockEntity(@NotNull EntityType entityType) {
        super(entityType, UUID.randomUUID());
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

    public abstract String getName();

    public void damage(SkyblockPlayer player) {
        PlayerMeleeDamageEntityEvent event = new PlayerMeleeDamageEntityEvent(player, this, player.getStat(Stat.Damage), player.getStat(Stat.Strength), player.getStat(Stat.CritDamage), player.getStat(Stat.CritChance));
        EventDispatcher.call(event);
        if (event.isCancelled()) return;
        float damage = (float) ((event.isCrit()) ? event.calculateCritHit() : event.calculateHit());
        damage = onDamage(damage);
        if (damage <= 0) return;
        spawnDamageTag(event);
        damage(DamageType.PLAYER_ATTACK, damage * (1 - (getDefense() / (getDefense() + 100))));
        if (canTakeKnockback())
            this.takeKnockback(0.4f, Math.sin(player.getPosition().yaw() * 0.017453292), -Math.cos(player.getPosition().yaw() * 0.017453292));
    }

    public static void spawnDamageTag(PlayerMeleeDamageEntityEvent event) {
        EntityCreature creature = new EntityCreature(EntityType.ARMOR_STAND, UUID.randomUUID());
        creature.setCustomName(Component.text(event.getDamageTag()));
        creature.setCustomNameVisible(true);
        creature.setInvisible(true);
        creature.setNoGravity(true);
        creature.setInvulnerable(true);
        ArmorStandMeta meta = (ArmorStandMeta) creature.getEntityMeta();
        meta.setHasNoBasePlate(true);
        meta.setMarker(true);
        BoundingBox bb = creature.getBoundingBox();
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
        Pos pos = event.getTarget().getPosition().add(x, 0.7d, z);
        creature.setInstance(event.getTarget().getInstance(), pos.add(0, new Random().nextDouble(0.5) - 0.25, 0));
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
                return STR."§8[§7Lv\{skyblockEntity.getLevel()}§8] §c\{skyblockEntity.getName()} §a\{StringUtils.cleanDouble(skyblockEntity.getHealth())}§7/§a\{StringUtils.cleanDouble(skyblockEntity.getMaxHealth())}§c\{Stat.Health.getSymbol()}";
            }
        }
    }

    public static void init() {

    }
}
