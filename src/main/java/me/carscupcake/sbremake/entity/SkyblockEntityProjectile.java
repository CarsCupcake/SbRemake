package me.carscupcake.sbremake.entity;

import lombok.Getter;
import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityVelocityPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

@Getter
public class SkyblockEntityProjectile extends EntityProjectile {
    private final double damage;
    private final double trueDamage;

    public SkyblockEntityProjectile(@NotNull SkyblockEntity shooter, @NotNull EntityType entityType) {
        this(shooter.getDamage(), shooter.getTrueDamage(), entityType, shooter.getPosition().add(0, shooter.getEyeHeight(), 0), shooter.getInstance(), shooter.getPosition().direction(), shooter);
    }

    public SkyblockEntityProjectile(double damage, double trueDamage, EntityType entityType, Pos pos, Instance instance, Vec shootingDir, @Nullable SkyblockEntity shooter) {
        super(shooter, entityType);
        this.damage = damage;
        this.trueDamage = trueDamage;
        setInstance(instance, pos);
        setVelocity(shootingDir.normalize().mul(40));
        setView((float) Math.toDegrees(Math.atan2(getVelocity().x(), getVelocity().z())), (float) Math.toDegrees(Math.atan2(getVelocity().y(), Math.sqrt(getVelocity().x() * getVelocity().x() + getVelocity().z() * getVelocity().z()))));
        setNoGravity(false);
        scheduler().buildTask(() -> {
            if (this.isRemoved() || this.getInstance() == null || this.getVelocity() == Vec.ZERO) {
                return;
            }
            for (Player pl : this.getInstance().getPlayers().stream().filter(player1 -> player1.getDistance(this) < 16d * 8d).toList()) {
                pl.sendPacket(new EntityVelocityPacket(this.getEntityId(), this.getVelocity().mul(8000.0F / (float) ServerFlag.SERVER_TICKS_PER_SECOND)));
            }
        }).repeat(Duration.ofMillis(50)).schedule();
    }
}
