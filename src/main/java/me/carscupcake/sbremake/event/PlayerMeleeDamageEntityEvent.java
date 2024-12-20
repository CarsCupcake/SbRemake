package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;


public class PlayerMeleeDamageEntityEvent extends PlayerToEntityDamageEvent {

    public PlayerMeleeDamageEntityEvent(SkyblockPlayer player, SkyblockEntity target, double weaponDamage, double strength, double critDamage, double critChance, double ferocity) {
        super(player, target, weaponDamage, strength, critDamage, critChance, ferocity);

    }

    @Override
    public EntityDeathEvent.Type damageType() {
        return EntityDeathEvent.Type.Melee;
    }
}
