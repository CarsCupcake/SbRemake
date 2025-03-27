package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;

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

    public static final EventFilter<PlayerDamageEntityEvent, Player> DAMAGE_EVENT = EventFilter.from(PlayerDamageEntityEvent.class, Player.class, PlayerToEntityDamageEvent::getPlayer);
}
