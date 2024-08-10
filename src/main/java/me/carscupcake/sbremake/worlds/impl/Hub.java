package me.carscupcake.sbremake.worlds.impl;

import kotlin.Pair;
import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.entity.impl.hub.GraveyardZombie;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import me.carscupcake.sbremake.worlds.region.PolygonalRegion;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Hub extends SkyblockWorld.WorldProvider {
    public final HashMap<BlockVec, Log.LogInfo> brokenLogs = new HashMap<>();
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Hub;
    }

    @Override
    public Pos spawn() {
        return new Pos(-2.5, 70.5, -70.5, 180, 0);
    }

    @Override
    public me.carscupcake.sbremake.worlds.region.Region[] regions() {
        return Region.values();
    }

    @Override
    public boolean useCustomMining() {
        return false;
    }

    private final Set<EntitySpawner> spawners = new HashSet<>();
    private final TaskScheduler foragingReset = new TaskScheduler() {
        @Override
        public void run() {
            brokenLogs.forEach((block, log) -> log.regen(container, block));
            brokenLogs.clear();
        }
    };

    @Override
    protected void register() {
        foragingReset.repeatTask(20 * 30);
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-105.5, 71.0, -61.5), new Pos(-112.5, 71.0, -61.5), new Pos(-114.5, 71.0, -66.5), new Pos(-117.5, 71.0, -73.5), new Pos(-124.5, 71.0, -75.5), new Pos(-121.5, 71.0, -82.5), new Pos(-117.5, 71.0, -89.5), new Pos(-110.5, 72.0, -103.5), new Pos(-101.5, 72.0, -111.5), new Pos(-99.5, 72.0, -127.5), new Pos(-93.5, 72.0, -145.5), new Pos(-119.5, 72.0, -141.5), new Pos(-145.5, 72.0, -122.5), new Pos(-158.5, 72.0, -93.5), new Pos(-173.5, 74.0, -86.5), new Pos(-163.5, 72.0, -136.5), new Pos(-68.5, 79.0, -184.5), new Pos(-43.5, 80.0, -173.5)}, 200,
                new EntitySpawner.BasicConstructor(GraveyardZombie::new), container));
    }

    @Override
    protected void unregister() {
        foragingReset.cancel();
        spawners.forEach(EntitySpawner::stop);
    }

    public enum Region implements me.carscupcake.sbremake.worlds.region.Region {
        ArcheryRange("§9Archery Range", new Pair<>(new Pos(-12, 67, -130), new Pos(6, 65.22, 155))),
        Graveyard("§cGraveyard", new Pos(-99.0, 75.0, -62.0),new Pos(-92.0, 74.0, -68.0),new Pos(-88.0, 73.0, -81.0),new Pos(-82.0, 74.0, -98.0),new Pos(-77.0, 75.0, -120.0),new Pos(-52.0, 74.0, -136.0),new Pos(-40.0, 80.0, -173.0),new Pos(-41.0, 70.0, -228.0),new Pos(-107.0, 70.0, -217.0),new Pos(-147.0, 70.0, -196.0),new Pos(-178.0, 67.0, -153.0),new Pos(-214.0, 69.0, -96.0),new Pos(-207.0, 77.0, -81.0),new Pos(-173.0, 78.0, -76.0),new Pos(-145.0, 75.0, -77.0),new Pos(-130.0, 80.0, -61.0),new Pos(-108.0, 75.0, -53.0)),
        Forest("§bForest", new Pos(-92.0, 70.0, -18.0), new Pos(-89.0, 70.0, -32.0), new Pos(-92.0, 70.0, -45.0), new Pos(-103.0, 70.0, -51.0), new Pos(-113.0, 75.0, -52.0), new Pos(-122.0, 76.0, -54.0), new Pos(-129.0, 78.0, -59.0), new Pos(-138.0, 80.0, -69.0), new Pos(-147.0, 74.0, -77.0), new Pos(-165.0, 77.0, -75.0), new Pos(-182.0, 77.0, -78.0), new Pos(-206.0, 77.0, -81.0), new Pos(-209.0, 71.0, -77.0), new Pos(-222.0, 65.0, -58.0), new Pos(-225.0, 72.0, -23.0), new Pos(-228.0, 73.0, -6.0), new Pos(-201.0, 74.0, 0.0), new Pos(-176.0, 72.0, 3.0), new Pos(-157.0, 69.0, 16.0), new Pos(-145.0, 70.0, 27.0), new Pos(-134.0, 70.0, 23.0), new Pos(-125.0, 70.0, 17.0), new Pos(-113.0, 70.0, -3.0), new Pos(-100.0, 70.0, -20.0)),
        CoalMine("§bCoal Mine", new Pair<>(new Pos(-36, 0, -231), new Pos(5, 200, -156)));
        private final me.carscupcake.sbremake.worlds.region.Region wrapped;

        @SafeVarargs
        Region(String name, Pair<Pos, Pos>... positions) {
            Set<BoundingBox> box = new HashSet<>();
            for (Pair<Pos, Pos> posPair : positions)
                box.add(BoundingBox.fromPoints(posPair.getFirst(), posPair.getSecond()));
            wrapped = new CuboidRegion(name, box);
        }

        Region(String name, Pos... positions) {
            pickP0(positions);
            wrapped = new PolygonalRegion(name, positions);
        }

        private static void pickP0(Pos[] pos2ds) {
            int minX = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minI = 0;
            for (int i = 0; i < pos2ds.length; i++) {
                if (pos2ds[i].x() < minX || (pos2ds[i].x() == minX && pos2ds[i].y() > maxY)) {
                    minX = pos2ds[i].blockX();
                    maxY = pos2ds[i].blockY();
                    minI = i;
                }
            }
            if (minI == 0) return;
            Pos[] copy = Arrays.copyOf(pos2ds, pos2ds.length);
            int j = 0;
            for (int i = minI; i < pos2ds.length; i++) {
                pos2ds[i] = copy[j];
                j++;
            }
            for (int i = 0; i < minI; i++) {
                pos2ds[i] = copy[j];
                j++;
            }
        }

        @Override
        public boolean isInRegion(Point pos) {
            return wrapped.isInRegion(pos);
        }

        @Override
        public String toString() {
            return wrapped.name();
        }
    }
}
