package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.SkyblockPlayerArrow;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@Getter
public class PlayerProjectileDamageEntityEvent extends PlayerToEntityDamageEvent {
    private final SkyblockPlayerArrow projectile;

    public PlayerProjectileDamageEntityEvent(SkyblockEntity target, SkyblockPlayerArrow projectile) {
        super((SkyblockPlayer) projectile.getShooter(), target, projectile.getWeaponDamage(), projectile.getStrength(), projectile.getCritDamage(), projectile.getCritChance());
        this.projectile = projectile;
    }

}
