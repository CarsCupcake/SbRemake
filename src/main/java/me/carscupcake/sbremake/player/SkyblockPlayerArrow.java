package me.carscupcake.sbremake.player;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.arrows.SkyblockArrow;
import me.carscupcake.sbremake.util.ParticleUtils;
import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityVelocityPacket;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

@Getter
public class SkyblockPlayerArrow extends EntityProjectile {
    private final SkyblockArrow arrow;
    private final double weaponDamage;
    private final double strength;
    private final double critDamage;
    private final double critChance;
    private final boolean canCrit;

    public SkyblockPlayerArrow(@NotNull SkyblockPlayer player, SkyblockArrow arrow, boolean crits) {
        super(player, EntityType.ARROW);
        this.arrow = arrow;
        scheduler().buildTask(() -> {
            if (this.isRemoved() || this.getInstance() == null || this.getVelocity() == Vec.ZERO) {
                return;
            }
            for (Player pl : this.getInstance().getPlayers().stream().filter(player1 -> player1.getDistance(this) < 16d * 8d).toList()) {
                pl.sendPacket(new EntityVelocityPacket(this.getEntityId(), this.getVelocity().mul((double) (8000.0F / (float) ServerFlag.SERVER_TICKS_PER_SECOND))));
            }
            if (crits) ParticleUtils.spawnParticle(this.getInstance(), this.getPosition(), Particle.CRIT, 1);
        }).repeat(Duration.ofMillis(50)).schedule();
        weaponDamage = player.getStat(Stat.Damage);
        strength = player.getStat(Stat.Strength);
        critDamage = player.getStat(Stat.CritDamage);
        critChance = player.getStat(Stat.CritChance);
        canCrit = crits;
    }

    @Override
    public void tick(long time) {
        if (this.instance == null) {
            if (!this.isRemoved()) this.remove();
            return;
        }
        super.tick(time);
    }

    public static void launchArrow(SkyblockPlayer player) {
        launchArrow(player, 1000L, (SkyblockArrow) SbItemStack.base(Material.ARROW).sbItem());
    }

    public static void launchArrow(SkyblockPlayer player, long chargeTime, SkyblockArrow arrow) {
        double chargingSeconds = Math.min(1d, chargeTime / 1000d);
        SkyblockPlayerArrow projectile = new SkyblockPlayerArrow(player, arrow, chargingSeconds >= 1);
        var pos = player.getPosition().add(0D, player.getEyeHeight(), 0D);
        projectile.setInstance(player.getInstance(), pos);
        projectile.setVelocity(player.getPosition().direction().normalize().mul(60 * chargingSeconds));
        projectile.setView((float) Math.toDegrees(Math.atan2(projectile.getVelocity().x(), projectile.getVelocity().z())), (float) Math.toDegrees(Math.atan2(projectile.getVelocity().y(), Math.sqrt(projectile.getVelocity().x() * projectile.getVelocity().x() + projectile.getVelocity().z() * projectile.getVelocity().z()))));
        projectile.setNoGravity(false);
    }
}
