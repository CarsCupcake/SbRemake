package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.trait.CancellableEvent;

@Setter
@Getter
public class PlayerSelfDamageEvent implements CancellableEvent {
    private final SkyblockPlayer player;
    private double normalDamage;
    private double trueDamage;
    private boolean cancelled = false;
    public PlayerSelfDamageEvent(SkyblockPlayer player, double damage, double trueDamage) {
        this.player = player;
        normalDamage = damage;
        this.trueDamage = trueDamage;
    }
    public double calculateDamage() {
        double defense = player.getStat(Stat.Defense);
        double trueDefense = player.getStat(Stat.TrueDefense);
        double damageReduction = defense / (defense + 100d);
        double trueDamageReduction = trueDefense / (trueDefense + 100);
        return normalDamage * (1d + damageReduction) + trueDamage * (1d + trueDamageReduction);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
