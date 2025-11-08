package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.event.Event;

import java.time.Duration;
import java.util.function.Consumer;

public interface IDamageEvent extends Event {
    default void spawnDamageTag() {
        LivingEntity e = new LivingEntity(EntityType.ARMOR_STAND);
        e.setCustomName(Component.text("§7" + (StringUtils.cleanDouble(getCachedDamage(), 0))));
        e.setCustomNameVisible(true);
        e.setInvisible(true);
        e.setNoGravity(true);
        e.setInvulnerable(true);
        ArmorStandMeta meta = (ArmorStandMeta) e.getEntityMeta();
        meta.setHasNoBasePlate(true);
        meta.setMarker(true);
        e.setInstance(getTarget().getInstance(), getTarget().getPosition().add(0, getTarget().getEyeHeight() / 2, 0));
        e.scheduleRemove(Duration.ofSeconds(2));
    }

    double getCachedDamage();

    void setCachedDamage(double damage);

    Entity getTarget();

    Entity getSource();

    double getTrueDamage();

    double getNormalDamage();

    double getDefense();

    double getTrueDefense();
    void setDefense(double d);
    void setTrueDefense(double d);

    double getAdditiveMultiplier();
    double getMultiplicativeMultiplier();
    void addAdditiveMultiplier(double d);
    void addMultiplicativeMultiplier(double d);

    default double calculateDamage() {
        double defense = getDefense();
        double trueDefense = getTrueDefense();
        double damageReduction = defense / (defense + 100d);
        double trueDamageReduction = trueDefense / (trueDefense + 100);
        setCachedDamage(((getNormalDamage() * (1d - damageReduction)) + (getTrueDamage() * (1d - trueDamageReduction))) * getAdditiveMultiplier() * getMultiplicativeMultiplier());
        return getCachedDamage();
    }

    void onDamageFinalize(Consumer<Double> consumer);
    void triggerFinalizer(double damage);
}
