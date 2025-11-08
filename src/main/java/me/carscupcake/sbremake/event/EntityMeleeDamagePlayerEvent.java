package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class EntityMeleeDamagePlayerEvent extends EntityDamagePlayerEvent {

    public EntityMeleeDamagePlayerEvent(SkyblockEntity entity, SkyblockPlayer player) {
        super(entity, player, entity.getDamage(), entity.getTrueDamage());
    }

    public EntityMeleeDamagePlayerEvent(SkyblockEntity entity, SkyblockPlayer player, double damage, double trueDamage) {
        super(entity, player, damage, trueDamage);
    }


}
