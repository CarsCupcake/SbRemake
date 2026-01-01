package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class EntityUtils {
    public static @Nullable SkyblockEntity getSkyblockEntityInLine(Pos first, Pos second, Instance instance, double boundingBoxMultiplier) {
        return (SkyblockEntity) getEntitiesInLine(first, second, instance, boundingBoxMultiplier).stream().filter(e -> e instanceof SkyblockEntity).findFirst().orElse(null);
    }

    public static Set<Entity> getEntitiesInLine(Pos first, Pos second, Instance instance, double boundingBoxMultiplier) {
        Set<Entity> entities = new TreeSet<>((o1, o2) -> (int) (o1.getPosition().distanceSquared(first) - o2.getPosition().distanceSquared(first)));
        Line line = new Line(first.asVec(), second.sub(first).asVec());
        int minX = min(first.chunkX(), second.chunkX());
        int maxX = max(first.chunkX(), second.chunkX());
        int minZ = min(first.chunkZ(), second.chunkZ());
        int maxZ = max(first.chunkZ(), second.chunkZ());
        double dis = second.distance(first);
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                Chunk chunk = instance.getChunk(x, z);
                if (chunk == null) continue;
                for (Entity e : instance.getChunkEntities(chunk)) {
                    if (e instanceof SkyblockPlayer) continue;
                    BoundingBox aabb = new BoundingBox(e.getBoundingBox().width() * boundingBoxMultiplier, e.getBoundingBox().height() * boundingBoxMultiplier, e.getBoundingBox().depth() * boundingBoxMultiplier);
                    for (Vec v : line.getCollidePoints(aabb.withOffset(e.getPosition().sub(aabb.width() / 2, 0, aabb.depth() / 2)), true)) {
                        if (first.distance(v) <= dis) {
                            entities.add(e);
                            break;
                        }
                    }

                }
            }
        }
        return entities;
    }

    public static boolean blocksInSight(Instance instance, Pos pos, Vec dir, double distance) {
        Vec normal = dir.normalize();
        Line l = new Line(pos.asVec(), dir);
        for (int i = 0; i < (int) distance; i++) {
            pos = pos.add(normal);
            Block block = instance.getBlock(pos);
            if (block.isSolid()) {
                if (block.registry().collisionShape() instanceof BoundingBox bb && !l.collidesWithBB(bb.withOffset(new BlockVec(pos.blockX(), pos.blockY(), pos.blockZ()))))
                    continue;
                return true;
            }
        }
        return false;
    }
}

