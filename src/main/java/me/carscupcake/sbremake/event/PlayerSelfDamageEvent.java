package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Setter
@Getter
public class PlayerSelfDamageEvent implements CancellableEvent, IDamageEvent {
    private final SkyblockPlayer player;
    private double normalDamage;
    private double trueDamage;
    private double multiplicativeMultiplier = 1;
    private double additiveMultiplier = 1;
    private boolean cancelled = false;
    private final Set<Consumer<Double>> finalizers = new HashSet<>();

    public PlayerSelfDamageEvent(SkyblockPlayer player, double damage, double trueDamage) {
        this.player = player;
        normalDamage = damage;
        this.trueDamage = trueDamage;
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
        return player;
    }

    @Override
    public double getDefense() {
        return player.getStat(Stat.Defense);
    }

    @Override
    public double getTrueDefense() {
        return player.getStat(Stat.TrueDefense);
    }

    @Override
    public void setDefense(double d) {

    }

    @Override
    public void setTrueDefense(double d) {

    }

    @Override
    public double getAdditiveMultiplier() {
        return additiveMultiplier;
    }

    @Override
    public double getMultiplicativeMultiplier() {
        return multiplicativeMultiplier;
    }

    @Override
    public void addAdditiveMultiplier(double d) {
        this.additiveMultiplier += d;
    }

    @Override
    public void addMultiplicativeMultiplier(double d) {
        this.multiplicativeMultiplier *= d;
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
