package me.carscupcake.sbremake.worlds.impl;


import lombok.Getter;
import me.carscupcake.sbremake.blocks.FarmingCrystal;
import me.carscupcake.sbremake.entity.impl.farmingIsles.*;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import me.carscupcake.sbremake.worlds.region.PolygonalRegion;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;

import java.util.*;

@Getter
public class FarmingIsles extends SkyblockWorld.WorldProvider {
    public static final String FARMING_CRYSTAL_SKIN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI2NWY5NmY1NGI3ODg4NWM0NmU3ZDJmODZiMWMxZGJmZTY0M2M2MDYwZmM3ZmNjOTgzNGMzZTNmZDU5NTEzNSJ9fX0=";
    private List<FarmingCrystal> crystals = new ArrayList<>();
    private final MapList<Integer, BlockVec> sugarCane = new MapList<>();
    private final TaskScheduler sugarCaneRespawn = new TaskScheduler() {
        @Override
        public void run() {
            for (List<BlockVec> blockVecs : sugarCane.values())
                for (BlockVec block : blockVecs)
                    container.setBlock(block, Block.SUGAR_CANE);
            sugarCane.clear();
        }
    };

    public FarmingIsles() {
        super(List.of(new Launchpad(141, -313, 143, -311, 91, SkyblockWorld.FarmingIsles, new Pos(153.5, 77, -362)),
                new Launchpad(149, -359, 151, -361, 75, SkyblockWorld.FarmingIsles, new Pos(142, 92, -308))));
    }

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.FarmingIsles;
    }

    @Override
    public Pos spawn() {
        return new Pos(113.5, 71, -207.5, -135, 0);
    }

    @Override
    public Region[] regions() {
        return Region.values();
    }

    private final HashSet<EntitySpawner> spawners = new HashSet<>();

    @Override
    protected void register() {
        crystals = List.of(new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(125.5, 78, -216.5), container), new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(148.5, 86, -278.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(121.5, 82, -270.5), container), new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(168.5, 87, -264.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(174.5, 90, -244.5), container), new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(172.5, 85, -218.5), container));
        sugarCaneRespawn.repeatTask(200);
        spawners.add(new EntitySpawner(new Pos[]{new Pos(162.0, 73.0, -232.0), new Pos(166.0, 74.0, -236.0), new Pos(172.0, 75.0, -239.0), new Pos(95.0, 70.0, -240.0), new Pos(106.0, 71.0, -239.0), new Pos(113.0, 72.0, -245.0), new Pos(116.0, 73.0, -257.0), new Pos(150.0, 71.0, -217.0), new Pos(149.0, 72.0, -223.0)}, 20 * 5, new EntitySpawner.BasicConstructor(Cow::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(181.0, 78.0, -246.0), new Pos(171.0, 75.0, -243.0), new Pos(158.0, 73.0, -232.0), new Pos(156.0, 72.0, -223.0), new Pos(153.0, 71.0, -214.0), new Pos(148.0, 71.0, -220.0), new Pos(124.0, 72.0, -244.0), new Pos(123.0, 73.0, -255.0), new Pos(110.0, 72.0, -253.0), new Pos(143.0, 73.0, -250.0)}, 20 * 5, new EntitySpawner.BasicConstructor(Pig::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(164.0, 74.0, -240.0), new Pos(180.0, 77.0, -244.0), new Pos(156.0, 72.0, -223.0), new Pos(152.0, 72.0, -225.0), new Pos(147.0, 71.0, -215.0), new Pos(141.0, 73.0, -250.0), new Pos(145.0, 73.0, -250.0), new Pos(143.0, 80.0, -246.0), new Pos(142.0, 74.0, -257.0), new Pos(123.0, 73.0, -253.0), new Pos(124.0, 72.0, -244.0), new Pos(106.0, 71.0, -239.0)}, 20 * 5, new EntitySpawner.BasicConstructor(Chicken::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(112.0, 64.0, -422.0), new Pos(118.0, 64.0, -418.0), new Pos(121.0, 64.0, -426.0), new Pos(100.0, 64.0, -431.0), new Pos(103.0, 64.0, -455.0), new Pos(116.0, 64.0, -476.0), new Pos(168.0, 76.0, -467.0), new Pos(180.0, 76.0, -469.0), new Pos(182.0, 76.0, -465.0), new Pos(178.0, 76.0, -486.0), new Pos(140.0, 75.0, -508.0), new Pos(135.0, 75.0, -498.0), new Pos(105.0, 75.0, -512.0)},
                20 * 5, new EntitySpawner.BasicConstructor(Sheep::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(123.0, 64.0, -470.0), new Pos(118.0, 64.0, -476.0), new Pos(117.0, 64.0, -470.0), new Pos(108.0, 64.0, -475.0), new Pos(110.0, 64.0, -467.0), new Pos(114.0, 64.0, -447.0), new Pos(106.0, 64.0, -448.0), new Pos(99.0, 64.0, -449.0), new Pos(93.0, 64.0, -442.0), new Pos(101.0, 64.0, -430.0), new Pos(114.0, 64.0, -427.0), new Pos(113.0, 64.0, -420.0), new Pos(119.0, 64.0, -427.0), new Pos(123.0, 64.0, -421.0), new Pos(179.0, 76.0, -445.0), new Pos(182.0, 76.0, -448.0), new Pos(182.0, 76.0, -453.0), new Pos(180.0, 76.0, -468.0), new Pos(168.0, 76.0, -467.0)},
                200, new EntitySpawner.BasicConstructor(Rabbit::new), container));
    }

    @Override
    protected void unregister() {
        crystals.forEach(farmingCrystal -> farmingCrystal.task().cancel());
        sugarCaneRespawn.cancel();
    }

    @Override
    public boolean useCustomMining() {
        return false;
    }

    public enum Region implements me.carscupcake.sbremake.worlds.region.Region {
        Oasis("§bOasis", new Pair<>(new Pos(86, 110, -524), new Pos(189, 61, -430)), new Pair<>(new Pos(165, 110, -417), new Pos(86, 61, -434)));
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
