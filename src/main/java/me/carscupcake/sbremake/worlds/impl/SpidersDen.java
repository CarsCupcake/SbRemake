package me.carscupcake.sbremake.worlds.impl;

import kotlin.Pair;
import me.carscupcake.sbremake.entity.impl.spidersDen.DasherSpider;
import me.carscupcake.sbremake.entity.impl.spidersDen.SpiderJockey;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import me.carscupcake.sbremake.worlds.region.PolygonalRegion;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpidersDen extends SkyblockWorld.WorldProvider {
    public SpidersDen() {
        super(List.of(new Launchpad(-199, -230, -195, -226, 82, SkyblockWorld.Hub, new Pos(-160, 75, -156))));
    }

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.SpidersDen;
    }

    @Override
    public Pos spawn() {
        return new Pos(-200.5, 83, -231.5, 135, 0);
    }

    @Override
    public Region[] regions() {
        return Region.values();
    }

    private final Set<EntitySpawner> entitySpawners = new HashSet<>();
    @Override
    protected void register() {
        super.register();
        entitySpawners.add(new EntitySpawner(new Pos[]{new Pos(-195.0, 78.0, -271.0),new Pos(-202.0, 81.0, -285.0),new Pos(-215.0, 81.0, -294.0),new Pos(-203.0, 84.0, -310.0),new Pos(-242.0, 95.0, -309.0),new Pos(-246.0, 95.0, -319.0),new Pos(-225.0, 111.0, -331.0),new Pos(-222.0, 111.0, -325.0),new Pos(-195.0, 107.0, -306.0),new Pos(-182.0, 114.0, -290.0),new Pos(-205.0, 120.0, -282.0),new Pos(-221.0, 133.0, -299.0),new Pos(-208.0, 175.0, -319.0)},
                200, new EntitySpawner.BasicConstructor(() -> new DasherSpider(1)), this.container));
        entitySpawners.add(new EntitySpawner(new Pos[]{new Pos(-216.0, 81.0, -290.0),new Pos(-203.0, 81.0, -288.0),new Pos(-245.0, 95.0, -311.0),new Pos(-244.0, 95.0, -317.0),new Pos(-227.0, 103.0, -286.0),new Pos(-209.0, 103.0, -268.0),new Pos(-193.0, 94.0, -261.0),new Pos(-182.0, 94.0, -271.0),new Pos(-159.0, 109.0, -293.0),new Pos(-163.0, 133.0, -320.0),new Pos(-175.0, 114.0, -348.0),new Pos(-196.0, 133.0, -344.0),new Pos(-209.0, 133.0, -334.0),new Pos(-222.0, 133.0, -308.0),new Pos(-217.0, 155.0, -322.0),new Pos(-196.0, 176.0, -325.0),new Pos(-186.0, 176.0, -323.0)},
                250, new EntitySpawner.BasicConstructor(() -> new SpiderJockey(true)), this.container));
    }

    @Override
    protected void unregister() {
        super.unregister();
        entitySpawners.forEach(EntitySpawner::stop);
        entitySpawners.clear();
    }

    public enum Region implements me.carscupcake.sbremake.worlds.region.Region {
        SpiderMound("§cSpider Mound", new Pair<>(new Pos(-141, 77, -257), new Pos(-243, 200, -365))),
        ArachnesSanctuary("§4Arachne's Sanctuary", new Pair<>(new Pos(-246, 46, -205), new Pos(-317, 85, -147))),
        ArachnesBurrow("§4Arachne's Burrow", new Pair<>(new Pos(-366, 87, -198), new Pos(-293, 37, -338)), new Pair<>(new Pos(-185, 73, -232), new Pos(-293, 37, -338)),
                new Pair<>(new Pos(-222, 74, -231), new Pos(-356, 37, -191))),
        SpidersDen("§cSpider's Den", new Pair<>(new Pos(-401, 0, -363), new Pos(-128, 300, -132)));
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
            wrapped = new PolygonalRegion(name, positions, 300, -64);
        }
        Region(String name, int maxY, int minY, Pos... positions) {
            pickP0(positions);
            wrapped = new PolygonalRegion(name, positions, maxY, minY);
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
