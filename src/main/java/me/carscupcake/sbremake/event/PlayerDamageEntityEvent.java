package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;

@Getter
@Setter
public class PlayerDamageEntityEvent extends PlayerToEntityDamageEvent {
    private double damage;
    public PlayerDamageEntityEvent(SkyblockPlayer player, SkyblockEntity target, double damage) {
        super(player, target, 0, 0, 0, 0, 0);
        this.damage = damage;
    }

    @Override
    public double calculateCritHit() {
        return calculateHit();
    }

    @Override
    public double calculateHit() {
        return this.damage * getAdditiveMultiplier() * getMultiplicativeMultiplier() + getBonusModifier();
    }

    @Override
    public EntityDeathEvent.Type damageType() {
        return EntityDeathEvent.Type.Undefined;
    }
}
