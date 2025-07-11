package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;

@Setter
@Getter
public class PlayerSelfDamageEvent implements CancellableEvent, IDamageEvent {
    private final SkyblockPlayer player;
    private double normalDamage;
    private double trueDamage;
    private double multiplier = 1;
    private boolean cancelled = false;

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
    public double getTargetDefense() {
        return player.getStat(Stat.Defense);
    }

    @Override
    public double getTargetTrueDefense() {
        return player.getStat(Stat.TrueDefense);
    }
}
