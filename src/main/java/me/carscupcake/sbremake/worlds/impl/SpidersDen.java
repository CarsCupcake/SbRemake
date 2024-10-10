package me.carscupcake.sbremake.worlds.impl;

import kotlin.Pair;
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
