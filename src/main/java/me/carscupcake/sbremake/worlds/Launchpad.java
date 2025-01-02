package me.carscupcake.sbremake.worlds;

import com.google.common.util.concurrent.Futures;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public record Launchpad(int x1, int z1, int x2, int z2, int y, SkyblockWorld targetWorld, Pos targetPos) {
    public void launch(SkyblockPlayer player) {
        LivingEntity entity = new LivingEntity(EntityType.ARMOR_STAND);
        entity.setInvisible(true);
        entity.setInstance(player.getInstance(), player.getPosition());
        entity.addPassenger(player);
        Vec direction = targetPos.asVec().sub(entity.getPosition().asVec()).normalize().mul(1);
        int iterations = (int) (entity.getPosition().distance(targetPos) * 1) - 2;
        player.setOnLaunchpad(true);
        SkyblockWorld.WorldProvider provider = SkyblockWorld.getBestWorld(targetWorld);
        Future<SkyblockWorld.WorldProvider> instanceContainerFuture;
        if (provider == null) {
            provider = targetWorld.get();
            instanceContainerFuture = provider.initAsync();
        } else {
            instanceContainerFuture = Futures.immediateFuture(provider);
        }
        new TaskScheduler() {
            Pos p = entity.getPosition();
            int i = 0;

            @Override
            public void run() {
                if (i == iterations) {
                    cancel();
                    try {
                        SkyblockWorld.WorldProvider finalProvider = instanceContainerFuture.get();
                        synchronized (player) {
                            entity.removePassenger(player);
                            player.setOnLaunchpad(false);
                            if (player.getWorldProvider().type() == targetWorld) {
                                player.teleport(targetPos.withView(player.getPosition()));
                                return;
                            }
                            if (!finalProvider.isLoaded())
                                finalProvider.getOnStart().add(() -> player.setWorldProvider(finalProvider));
                            else player.setWorldProvider(finalProvider);
                            entity.remove();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    return;

                }
                p = p.add(direction);
                entity.teleport(p.add(0, sinus((double) i / iterations), 0));
                i++;
            }
        }.repeatTaskAsync(0, 1);
    }

    public boolean inBox(SkyblockPlayer player) {
        int minX = Math.min(x1, x2);
        int maxX = Math.max(x1, x2);
        int minZ = Math.min(z1, z2);
        int maxZ = Math.max(z1, z2);
        Pos pos = player.getPosition();
        if (pos.blockX() >= minX && pos.blockX() <= maxX && pos.blockY() == y + 1 && pos.blockZ() >= minZ && pos.blockZ() <= maxZ && player.isOnGround() && !player.isOnLaunchpad()) {
            return player.getInstance().getBlock(player.getPosition().sub(0, 1, 0)) == Block.SLIME_BLOCK;
        }
        return false;
    }

    private double sinus(double percentage) {
        return 20 * Math.sin(Math.PI * percentage);
    }
}
