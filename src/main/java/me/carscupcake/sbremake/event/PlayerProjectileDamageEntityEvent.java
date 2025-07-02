package me.carscupcake.sbremake.event;

import lombok.Getter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.SkyblockPlayerArrow;
import net.minestom.server.coordinate.Pos;

@Getter
public class PlayerProjectileDamageEntityEvent extends PlayerToEntityDamageEvent {
    private final SkyblockPlayerArrow projectile;

    public PlayerProjectileDamageEntityEvent(SkyblockEntity target, SkyblockPlayerArrow projectile) {
        super((SkyblockPlayer) projectile.getShooter(), target, projectile.getWeaponDamage(), projectile.getStrength(), projectile.getCritDamage(), projectile.getCritChance(), projectile.getFerocity());
        this.projectile = projectile;
    }

    @Override
    public Pos damagerPos() {
        return projectile.getPosition();
    }

    @Override
    public EntityDeathEvent.Type damageType() {
        return EntityDeathEvent.Type.Projectile;
    }
}
