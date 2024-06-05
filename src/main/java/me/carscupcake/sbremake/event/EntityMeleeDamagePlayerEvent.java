package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.trait.CancellableEvent;

@Getter
@Setter
public class EntityMeleeDamagePlayerEvent implements CancellableEvent {
    private final SkyblockEntity entity;
    private final SkyblockPlayer player;
    private double normalDamage;
    private double trueDamage;
    private boolean cancelled = false;
    public EntityMeleeDamagePlayerEvent(SkyblockEntity entity, SkyblockPlayer player) {
        this.player = player;
        this.entity = entity;
        normalDamage = entity.getDamage();
        trueDamage = entity.getTrueDamage();
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
