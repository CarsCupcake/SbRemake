package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.trait.CancellableEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Getter
@Setter
public abstract class EntityDamagePlayerEvent implements IDamageEvent, CancellableEvent {
    private boolean cancelled;
    private final SkyblockEntity source;
    private final SkyblockPlayer target;
    private double normalDamage;
    private double trueDamage;
    private double multiplicativeMultiplier = 1;
    private double additiveMultiplier = 1;
    private double defense;
    private double trueDefense;
    private final Set<Consumer<Double>> finalizers = new HashSet<>();
    private double cachedDamage;
    public EntityDamagePlayerEvent(SkyblockEntity source, SkyblockPlayer target, double damage, double trueDamage) {
        this.source = source;
        this.target = target;
        this.normalDamage = damage;
        this.trueDamage = trueDamage;
        this.defense = this.target.getStat(Stat.Defense, true);
        this.trueDefense = this.target.getStat(Stat.TrueDefense, true);
    }

    public void addMultiplicativeMultiplier(double multiplier) {
        this.multiplicativeMultiplier *= multiplier;
    }

    public void addAdditiveMultiplier(double multiplier) {
        this.multiplicativeMultiplier *= multiplier;
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
