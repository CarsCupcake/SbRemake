package me.carscupcake.sbremake.worlds.impl;

import kotlin.Pair;
import me.carscupcake.sbremake.entity.impl.deepCaverns.*;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import me.carscupcake.sbremake.worlds.region.PolygonalRegion;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeepCaverns extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.DeepCaverns;
    }

    @Override
    public Pos spawn() {
        return new Pos(4, 157, 85, 180, 0);
    }

    @Override
    public Region[] regions() {
        return Region.values();
    }

    private final Set<EntitySpawner> spawners = new HashSet<>();

    @Override
    protected void register() {
        spawners.add(new EntitySpawner(new Pos[]{new Pos(37.0, 119.0, 17.0),new Pos(21.0, 119.0, 7.0),new Pos(-3.0, 120.0, 18.0),new Pos(-9.0, 119.0, 28.0),new Pos(-18.0, 123.0, 25.0),new Pos(-32.0, 120.0, 25.0),new Pos(-30.0, 120.0, 7.0),new Pos(-44.0, 119.0, 14.0),new Pos(-34.0, 126.0, 32.0),new Pos(-55.0, 120.0, 39.0),new Pos(-42.0, 118.0, 46.0),new Pos(-34.0, 120.0, 30.0),new Pos(-1.0, 118.0, 8.0),new Pos(-4.0, 121.0, -5.0),new Pos(6.0, 123.0, -7.0),new Pos(24.0, 123.0, -4.0),new Pos(38.0, 122.0, -19.0),new Pos(52.0, 125.0, -25.0),new Pos(55.0, 126.0, -13.0)}
        , 200, new EntitySpawner.BasicConstructor(LapisZombies::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-39.0, 36.0, 36.0),new Pos(-51.0, 36.0, 43.0),new Pos(-54.0, 35.0, 52.0),new Pos(-63.0, 36.0, 29.0),new Pos(-57.0, 36.0, 11.0),new Pos(-50.0, 37.0, -6.0),new Pos(-34.0, 38.0, -27.0),new Pos(-21.0, 38.0, -34.0),new Pos(-2.0, 37.0, -35.0),new Pos(10.0, 36.0, -28.0),new Pos(11.0, 36.0, -20.0),new Pos(8.0, 35.0, -5.0),new Pos(-6.0, 34.0, 11.0),new Pos(3.0, 35.0, 23.0),new Pos(20.0, 36.0, 30.0),new Pos(21.0, 36.0, 53.0),new Pos(12.0, 35.0, 59.0),new Pos(38.0, 37.0, 16.0),new Pos(26.0, 36.0, 12.0),new Pos(9.0, 34.0, 12.0),new Pos(3.0, 35.0, 24.0),new Pos(-14.0, 34.0, 8.0),new Pos(-6.0, 34.0, 2.0)},
                200, new EntitySpawner.BasicConstructor(MinerZombie15::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(38.0, 37.0, 23.0),new Pos(30.0, 36.0, 35.0),new Pos(24.0, 36.0, 49.0),new Pos(11.0, 35.0, 59.0),new Pos(3.0, 37.0, 47.0),new Pos(-9.0, 37.0, 48.0),new Pos(-25.0, 36.0, 56.0),new Pos(-40.0, 35.0, 63.0),new Pos(-53.0, 35.0, 51.0),new Pos(-63.0, 35.0, 38.0),new Pos(-62.0, 36.0, 25.0),new Pos(-53.0, 36.0, 10.0),new Pos(-40.0, 34.0, 6.0),new Pos(-46.0, 37.0, -8.0),new Pos(-47.0, 37.0, -18.0),new Pos(-27.0, 38.0, -29.0),new Pos(-1.0, 37.0, -35.0),new Pos(42.0, 36.0, -18.0),new Pos(34.0, 34.0, 3.0),new Pos(5.0, 35.0, 23.0),new Pos(23.0, 36.0, 36.0)},
                200, new EntitySpawner.BasicConstructor(MinerSkeleton15::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(33.0, 11.0, 17.0),new Pos(18.0, 11.0, 24.0),new Pos(4.0, 13.0, 38.0),new Pos(-13.0, 11.0, 47.0),new Pos(-13.0, 11.0, 31.0),new Pos(-20.0, 10.0, 16.0),new Pos(-38.0, 9.0, 14.0),new Pos(-44.0, 9.0, 3.0),new Pos(-34.0, 11.0, -23.0),new Pos(-10.0, 11.0, -28.0),new Pos(3.0, 11.0, -24.0),new Pos(19.0, 11.0, -13.0),new Pos(5.0, 13.0, 34.0),new Pos(-6.0, 12.0, 39.0),new Pos(-16.0, 11.0, 41.0),new Pos(-43.0, 10.0, 48.0),new Pos(-59.0, 12.0, 47.0),new Pos(-69.0, 11.0, 38.0),new Pos(-68.0, 11.0, 21.0),new Pos(-64.0, 11.0, 4.0),new Pos(-73.0, 11.0, -8.0),new Pos(-66.0, 13.0, -22.0),new Pos(-48.0, 12.0, -25.0),new Pos(-32.0, 11.0, -21.0),new Pos(-21.0, 10.0, -4.0),new Pos(1.0, 10.0, 13.0),new Pos(18.0, 11.0, 23.0)}
                ,200, new EntitySpawner.BasicConstructor(MinerZombie20::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(33.0, 11.0, 17.0),new Pos(30.0, 11.0, 3.0),new Pos(12.0, 12.0, -15.0),new Pos(-13.0, 12.0, -27.0),new Pos(-28.0, 11.0, -17.0),new Pos(-39.0, 11.0, -20.0),new Pos(-52.0, 12.0, -25.0),new Pos(-73.0, 11.0, -6.0),new Pos(-63.0, 11.0, 2.0),new Pos(-42.0, 9.0, 5.0),new Pos(-47.0, 12.0, 22.0),new Pos(-41.0, 10.0, 40.0),new Pos(-42.0, 13.0, 60.0),new Pos(-64.0, 12.0, 43.0),new Pos(-68.0, 11.0, 20.0),new Pos(-70.0, 12.0, 4.0),new Pos(-68.0, 13.0, -20.0),new Pos(-32.0, 11.0, -22.0),new Pos(-1.0, 11.0, -26.0),new Pos(16.0, 11.0, 23.0),new Pos(13.0, 12.0, 16.0),new Pos(-9.0, 12.0, 18.0),new Pos(-23.0, 10.0, 5.0),new Pos(-23.0, 11.0, -10.0)},
                200, new EntitySpawner.BasicConstructor(MinerSkeleton20::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(3.0, 151.0, 24.0),new Pos(-14.0, 152.0, 34.0),new Pos(-16.0, 154.0, 19.0),new Pos(-26.0, 153.0, 26.0),new Pos(-33.0, 158.0, 33.0),new Pos(-39.0, 154.0, 8.0),new Pos(-19.0, 154.0, 8.0),new Pos(-24.0, 154.0, -22.0),new Pos(-9.0, 152.0, -30.0),new Pos(10.0, 154.0, -37.0),new Pos(30.0, 157.0, -30.0),new Pos(53.0, 159.0, -23.0),new Pos(57.0, 158.0, 2.0),new Pos(26.0, 155.0, 16.0),new Pos(17.0, 160.0, 0.0),new Pos(2.0, 154.0, 16.0),new Pos(13.0, 163.0, -7.0),new Pos(25.0, 160.0, -26.0)}
                , 200, new EntitySpawner.BasicConstructor(SneakyCreeper::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-21.0, 63.0, 28.0),new Pos(3.0, 64.0, -14.0),new Pos(23.0, 64.0, 29.0),new Pos(0.0, 64.0, -13.0)},
                200, new EntitySpawner.BasicConstructor(() -> new EmeraldSlime(15)), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(29.0, 64.0, 29.0),new Pos(26.0, 65.0, 54.0),new Pos(38.0, 69.0, 54.0),new Pos(13.0, 65.0, 63.0),new Pos(3.0, 64.0, 54.0),new Pos(-34.0, 64.0, 41.0),new Pos(-21.0, 64.0, 19.0),new Pos(15.0, 64.0, -3.0),new Pos(35.0, 64.0, 1.0),new Pos(32.0, 64.0, -20.0)},
                200, new EntitySpawner.BasicConstructor(() -> new EmeraldSlime(10)), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(14.0, 64.0, -2.0),new Pos(8.0, 66.0, -6.0),new Pos(-2.0, 65.0, -1.0),new Pos(-11.0, 65.0, 14.0),new Pos(25.0, 65.0, 36.0),new Pos(25.0, 65.0, 53.0),new Pos(19.0, 65.0, 62.0),new Pos(-5.0, 64.0, 49.0),new Pos(-28.0, 65.0, 47.0),new Pos(-25.0, 63.0, 33.0),new Pos(-10.0, 64.0, 37.0),new Pos(21.0, 64.0, 31.0),new Pos(39.0, 69.0, 45.0),new Pos(38.0, 69.0, 54.0),new Pos(-36.0, 66.0, 6.0)},
                200, new EntitySpawner.BasicConstructor(() -> new EmeraldSlime(5)), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(37.0, 100.0, 24.0),new Pos(36.0, 99.0, 13.0),new Pos(34.0, 97.0, 2.0),new Pos(23.0, 100.0, 3.0),new Pos(10.0, 101.0, -2.0),new Pos(11.0, 100.0, -12.0),new Pos(22.0, 101.0, -27.0),new Pos(37.0, 99.0, -23.0),new Pos(39.0, 98.0, -8.0),new Pos(-6.0, 99.0, -1.0),new Pos(4.0, 100.0, 14.0),new Pos(12.0, 97.0, 19.0),new Pos(22.0, 96.0, 36.0),new Pos(18.0, 101.0, 50.0),new Pos(-14.0, 106.0, -2.0),new Pos(-8.0, 88.0, -20.0),new Pos(2.0, 95.0, -21.0)},
                200, new EntitySpawner.BasicConstructor(RedstonePigman::new), container));
        super.register();
    }

    @Override
    protected void unregister() {
        spawners.forEach(EntitySpawner::stop);
        spawners.clear();
        super.unregister();
    }

    //Gunpowder 255 - 132
    //Lapis 132 - 115
    //Redstone 115 - 74
    //Emerald 74 - 44
    //Dia1 44 - 19
    //Dia1 / Obi 19 - 0
    public enum Region implements me.carscupcake.sbremake.worlds.region.Region {
        GunpowderMines("§bGunpowder Mines", new Pair<>(new Pos(80, 175, 50), new Pos(-64, 132, -67))),
        LapisQuarry("§bLapis Quarry", new Pair<>(new Pos(63, 132, 87), new Pos(-64, 115, -67))),
        PigmensDen("§bPigmen's Den", new Pair<>(new Pos(63, 115, 87), new Pos(-64, 74, -67))),
        Slimehill("§bSlimehill", new Pair<>(new Pos(63, 74, 87), new Pos(-64, 44, -67))),
        DiamondReserve("§bDiamond Reserve", new Pair<>(new Pos(63, 44, 87), new Pos(-64, 19, -67))),
        ObsidianSanctuary("§bObsidian Sanctuary", new Pair<>(new Pos(63, 19, 87), new Pos(-64, 0, -67)));
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
