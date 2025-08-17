package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntityProjectile;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Setter
@Getter
public class ProjectileDamagePlayerEvent implements CancellableEvent, IDamageEvent {
    private final SkyblockEntityProjectile entity;
    private final SkyblockPlayer player;
    private double normalDamage;
    private double trueDamage;
    private double multiplier = 1;
    private boolean cancelled = false;
    private final Set<Consumer<Double>> finalizers = new HashSet<>();

    public ProjectileDamagePlayerEvent(SkyblockEntityProjectile entity, SkyblockPlayer player) {
        this.player = player;
        this.entity = entity;
        normalDamage = entity.getDamage();
        trueDamage = entity.getTrueDamage();
    }

    private double cachedDamage;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    @Override
    public Entity getTarget() {
        return player;
    }

    @Override
    public Entity getSource() {
        return entity;
    }

    @Override
    public double getTargetDefense() {
        return player.getStat(Stat.Defense);
    }

    @Override
    public double getTargetTrueDefense() {
        return player.getStat(Stat.TrueDefense);
    }


    @Override
    public void onDamageFinalize(Consumer<Double> consumer) {
        finalizers.add(consumer);
    }

    @Override
    public void triggerFinalizer(double damage) {
        finalizers.forEach(consumer -> consumer.accept(damage));
    }
}
