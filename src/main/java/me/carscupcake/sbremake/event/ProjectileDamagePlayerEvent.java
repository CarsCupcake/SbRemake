package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntityProjectile;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.CancellableEvent;

@Setter
@Getter
public class ProjectileDamagePlayerEvent implements CancellableEvent, IDamageEvent {
    private final SkyblockEntityProjectile entity;
    private final SkyblockPlayer player;
    private double normalDamage;
    private double trueDamage;
    private double multiplier = 1;
    private boolean cancelled = false;

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
}
