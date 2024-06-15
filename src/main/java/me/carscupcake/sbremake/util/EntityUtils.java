package me.carscupcake.sbremake.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;

import java.util.HashSet;
import java.util.Set;

public class EntityUtils {
    public static Set<Entity> getEntitiesInLine(Pos first, Pos second, Instance instance) {
        Set<Entity> entities = new HashSet<>();
        Line line = new Line(first.asVec(), second.sub(first).asVec());
        double dis = second.distance(first);
        for (int x = first.chunkX(); x <= second.chunkX(); x++) {
            for (int z = first.chunkZ(); z <= second.chunkZ(); z++) {
                Chunk chunk = instance.getChunk(x, z);
                for (Entity e : instance.getChunkEntities(chunk)) {
                    for (Vec v : line.getCollidePoints(e.getBoundingBox().withOffset(e.getPosition()), true)) {
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
}

