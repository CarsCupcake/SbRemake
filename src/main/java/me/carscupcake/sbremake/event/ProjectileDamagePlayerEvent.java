package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.SkyblockEntityProjectile;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class ProjectileDamagePlayerEvent extends EntityDamagePlayerEvent {
    private final SkyblockEntityProjectile entity;

    public ProjectileDamagePlayerEvent(SkyblockEntityProjectile entity, SkyblockPlayer player) {
        super((SkyblockEntity) entity.getShooter(), player, entity.getDamage(), entity.getTrueDamage());
        this.entity = entity;
    }

    public SkyblockEntityProjectile getProjectile() {
        return entity;
    }
}
