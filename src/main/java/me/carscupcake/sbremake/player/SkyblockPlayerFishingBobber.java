package me.carscupcake.sbremake.player;

import lombok.Getter;
import me.carscupcake.sbremake.item.SbItemStack;
import net.minestom.server.ServerFlag;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityProjectile;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.entity.metadata.other.FishingHookMeta;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.EntityVelocityPacket;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

@Getter
public class SkyblockPlayerFishingBobber extends EntityProjectile {
    private final SkyblockPlayer player;
    private final SbItemStack item;

    public SkyblockPlayerFishingBobber(@NotNull SkyblockPlayer shooter) {
        super(shooter, EntityType.FISHING_BOBBER);
        this.player = shooter;
        item = shooter.getSbItemInHand(PlayerHand.MAIN);
        var meta = (FishingHookMeta) this.getEntityMeta();
        meta.setOwnerEntity(shooter);
        scheduler().buildTask(() -> {
            if (position.distance(player.getPosition()) > 33) {
                remove(true);
                return;
            }
            if (this.isRemoved() || this.getInstance() == null || this.getVelocity() == Vec.ZERO) {
                return;
            }
            if (instance.getBlock(getPosition()) == Block.WATER && instance.getBlock(getPosition().add(0, 1, 0)) == Block.AIR) {
                var space = position.y() - (double) position.blockY();
                setNoGravity(true);
                if (space < 0.8) setVelocity(new Vec(0, 0.05, 0));
                hasPhysics = false;
                onGround = true;
            } else
                for (Player pl : this.getInstance().getPlayers().stream().filter(player1 -> player1.getDistance(this) < 16d * 8d).toList()) {
                    pl.sendPacket(new EntityVelocityPacket(this.getEntityId(), this.getVelocity().mul(8000.0F / (float) ServerFlag.SERVER_TICKS_PER_SECOND)));
                }
        }).repeat(TimeUnit.SERVER_TICK.getDuration()).schedule();
    }

    @Override
    public void setVelocity(@NotNull Vec velocity) {
        if (instance.getBlock(getPosition()) == Block.WATER && instance.getBlock(getPosition().add(0, 1, 0)) == Block.AIR && position.y() - (double) position.blockY() > 0.8)
            return;
        super.setVelocity(velocity);
    }

    @Override
    protected void remove(boolean b) {
        super.remove(b);
        player.setPlayerFishingBobber(null);
    }
}
