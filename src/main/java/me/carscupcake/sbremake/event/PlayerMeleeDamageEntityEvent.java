package me.carscupcake.sbremake.event;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;


public class PlayerMeleeDamageEntityEvent extends PlayerToEntityDamageEvent {

    public PlayerMeleeDamageEntityEvent(SkyblockPlayer player, SkyblockEntity target, double weaponDamage, double strength, double critDamage, double critChance) {
        super(player, target, weaponDamage, strength, critDamage, critChance);

    }
}
