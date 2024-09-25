package me.carscupcake.sbremake.worlds.impl;

import kotlin.Pair;
import lombok.Getter;
import me.carscupcake.sbremake.blocks.FarmingCrystal;
import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.entity.impl.hub.CryptGhoul;
import me.carscupcake.sbremake.entity.impl.hub.GoldenGhoul;
import me.carscupcake.sbremake.entity.impl.hub.GraveyardZombie;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.arrows.FlintArrow;
import me.carscupcake.sbremake.item.impl.bow.ArtisanalShortbow;
import me.carscupcake.sbremake.item.impl.bow.Bow;
import me.carscupcake.sbremake.item.impl.bow.WitherBow;
import me.carscupcake.sbremake.item.impl.pickaxe.GoldenPickaxe;
import me.carscupcake.sbremake.item.impl.pickaxe.PromisingPickaxe;
import me.carscupcake.sbremake.item.impl.pickaxe.RookiePickaxe;
import me.carscupcake.sbremake.item.impl.sword.DiamondSword;
import me.carscupcake.sbremake.item.impl.sword.EndSword;
import me.carscupcake.sbremake.item.impl.sword.SpiderSword;
import me.carscupcake.sbremake.item.impl.sword.UndeadSword;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.ItemCost;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.*;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import me.carscupcake.sbremake.worlds.region.PolygonalRegion;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.Material;

import java.util.*;

@Getter
public class Hub extends SkyblockWorld.WorldProvider {
    public static final String FARMING_CRYSTAL_SKIN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI2NWY5NmY1NGI3ODg4NWM0NmU3ZDJmODZiMWMxZGJmZTY0M2M2MDYwZmM3ZmNjOTgzNGMzZTNmZDU5NTEzNSJ9fX0=";
    private List<FarmingCrystal> crystals = new ArrayList<>();
    public final HashMap<BlockVec, Log.LogInfo> brokenLogs = new HashMap<>();

    public Hub() {
        super(List.of(new Launchpad(-11, -232, -9, -231, 63, SkyblockWorld.GoldMines, new Pos(-4.5, 77, -272)),
                new Launchpad(-226, -15, -224, -17, 72, SkyblockWorld.Park, new Pos(-275.5, 82, -13.5)), new Launchpad(77, -183, 80, -186, 71, SkyblockWorld.FarmingIsles, new Pos(114.5, 71, -207))
                , new Launchpad(-162, -161, -165, -164, 72, SkyblockWorld.SpidersDen, new Pos(-200.5, 83, -231.5, 135, 0))));
        customEntry.put(SkyblockWorld.GoldMines, new Pos(-9.5, 64, -228.5, 0, 0));
        customEntry.put(SkyblockWorld.Park, new Pos(-221.5, 73, -16.5, 0, 0));
        customEntry.put(SkyblockWorld.SpidersDen, new Pos(-159.5, 73, -158.5, -45, 0));
    }

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
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-153.0, 58.0, -100.0),new Pos(-150.0, 57.0, -103.0),new Pos(-147.0, 56.0, -100.0),new Pos(-143.0, 55.0, -101.0),new Pos(-145.0, 57.0, -111.0),new Pos(-142.0, 58.0, -122.0),new Pos(-131.0, 57.0, -129.0),new Pos(-116.0, 53.0, -125.0),new Pos(-102.0, 48.0, -119.0),new Pos(-108.0, 45.0, -130.0),new Pos(-119.0, 41.0, -134.0),new Pos(-123.0, 41.0, -141.0),new Pos(-131.0, 41.0, -135.0),new Pos(-132.0, 45.0, -121.0),new Pos(-134.0, 48.0, -113.0),new Pos(-132.0, 50.0, -105.0),new Pos(-124.0, 48.0, -102.0),new Pos(-115.0, 45.0, -98.0),new Pos(-106.0, 47.0, -102.0),new Pos(-88.0, 46.0, -105.0),new Pos(-76.0, 46.0, -107.0),new Pos(-66.0, 49.0, -117.0),new Pos(-53.0, 53.0, -129.0),new Pos(-48.0, 57.0, -142.0),new Pos(-82.0, 44.0, -93.0),new Pos(-90.0, 43.0, -91.0),new Pos(-99.0, 38.0, -89.0),new Pos(-104.0, 38.0, -84.0),new Pos(-97.0, 38.0, -83.0),new Pos(-102.0, 38.0, -80.0),new Pos(-114.0, 42.0, -86.0),new Pos(-101.0, 40.0, -95.0)},
                200, new EntitySpawner.BasicConstructor(CryptGhoul::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-126, 42, -136), new Pos(-101, 39, -85)}, 200, new EntitySpawner.BasicConstructor(GoldenGhoul::new), container));
        crystals = List.of(new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(44.5, 75, -122.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(29.5, 75, -147.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(24.5, 75, -178.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(59.5, 75, -182.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(73.5, 75, -160.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(63.5, 75, -133.5), container));
        npcs = new Npc[]{new MerchantNpc(new Pos(-9, 68, -125), container, "Mine Merchant", new PlayerSkin("eyJ0aW1lc3RhbXAiOjE1NTA2Nzg1NjA4ODUsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzYwODk5MDY1NjQ5MmQ3MjJhOGVhODk2YjgxNmQwMTkxMTM2OGIzODdhMmFjNGJjNzRmNzBhMGFlZGQ3ZWI3ZjgifX19", "M1H74ucEXCmd/ws7LGJSVaek4/4qsXn1Cj5HkuOg0Z7atseANP3NSQGoQF/vjuAOu8pMt/hE6oXfxX2XjsTysVpFxKWho+fVAObgCrkep9xaEZL7BvJ+zfFA+Gb3CAWPl4DtvjC0Jr7iWUo+sdlyWYb4HbipImwSUMVqPE0H0NC/aIlwiC19TlRNhS8WSP9IrlYQ1F50/rRRIgE3VfCyXQ85L7nk/ZjkKnNOmB/ekENSx9PqCbPSJV7nDjYpZAC9aT/f+kU2EuTuSptjomcWAcEgkBee2QZfnHkT79e41ezvEp4ZYfWqjJn2cQOTFNKbvk9Pl3jRUvXeTiDUkL15pWmJWhnhtW9sBzoP3JW7JXvUSQCeqVtNW9eQjeWhm0q2xV28KqTMYmwB3ZPh0lihJv79ae7u2Zretr8GrC2fScD/GJFjpz3JGIIWpiRogp/KY08y2KT07AzatrGjTJVlgTQCyJqIEIy1EmUoMq50i8EYyPR8FA7JvHKiiRheb/97vj0CwqRfA4nFMC5iLpMRJEjNXoeg7t/pau6Y6GA4lXJQcxourKsiTrbq+Mww3yery0Q3HNCDcgvbCOWCMdoLdL9FdC3POI9C+t7Nqh271lxY39NhT8LQ1ZQ+V57dqUq7EBwz/fq8VHpR8xb4JBdhPL+Ksb6IiAt6xmZ1inOJCJs="),
                List.of(new Pair<>(Objects.requireNonNull(SbItemStack.base(Material.COAL).withAmount(2)), new CoinsCost(8)), new Pair<>(Objects.requireNonNull(SbItemStack.base(Material.IRON_INGOT).withAmount(4)), new CoinsCost(22))
                        , new Pair<>(Objects.requireNonNull(SbItemStack.base(Material.GOLD_INGOT).withAmount(2)), new CoinsCost(12)),
                        new Pair<>(ISbItem.get(RookiePickaxe.class).create(), new CoinsCost(12)), new Pair<>(ISbItem.get(PromisingPickaxe.class).create(), new CoinsCost(35)), new Pair<>(ISbItem.get(GoldenPickaxe.class).create(), new ItemCost(ISbItem.get(Material.GOLD_INGOT), 3)),
                        new Pair<>(ISbItem.get(Material.TORCH).create().withAmount(32), new CoinsCost(16)), new Pair<>(ISbItem.get(Material.GRAVEL).create().withAmount(2), new CoinsCost(12)), new Pair<>(ISbItem.get(Material.COBBLESTONE).create(), new CoinsCost(3)),
                        new Pair<>(ISbItem.get(Material.STONE).create().withAmount(2), new CoinsCost(4)))),
        new MerchantNpc(new Pos(-10, 68, -130), container, "Weaponsmith", new PlayerSkin("eyJ0aW1lc3RhbXAiOjE1NTA2Nzg0NTQ3MTYsInByb2ZpbGVJZCI6ImEyZjgzNDU5NWM4OTRhMjdhZGQzMDQ5NzE2Y2E5MTBjIiwicHJvZmlsZU5hbWUiOiJiUHVuY2giLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FmM2Y3YTY5YzlhN2FkOTVlOTBmMjEyZGEzMDQ5OTBjOGJlMzZlOTMxOTdkYjg3NGMyYjBmZTA4MTA2ZTMyM2IifX19", "Sw5WHUwuYX2OxF/U1BHPpFdAJzwzzyzZ78bxQkdCQL1Jyd9RsIiQWKJSeTHsnhScpHxXEBtWKQ71YOci6HzwLiYZOh/M6KJ3kY+RtlRdh8EUEVj1BCoo8xvwAt28Piiqke3uDs9dWEvVBzs1PR9qxvMaarw3DT8sTgxIBU/xMmt41uDiCOgn6M/rL3waFsnPdN+v2tTHsl1aNz+hMLn4NuIbBprE90X0tsT/qlQzCBPHuwUV8elGb9xAfjJ0eRSlH1jxcfEJdb+YsPKwfqQ6btwkCOK2hdfm9/S6/Fd2KJsaDhH5twfykFyD1PEn4sCAlAisibKiOADQcg6lYN7d2eE95RVYhlqHTsb6g0aBUSk8Gj8OOABbBB0sEhGPmzTKJOAoyu9ZWgfzAty/ipGooyL/gWlGByTmWnmXf0ek6TUtkPpmLNd3Ik364+GDI+C8H14Dltc7axQXh0GsFmepUL2t/fz0fDtZlqfhwz3ei7q7fJ8S20l2O4y/GKDCItqMTkY80rOEQuysln13pV32z/8oZwl7a1rGSvSjLQwC+cpbhA8tATTVu/ovUd5Aev+PUb5vqrh4DmF4Br5cRxLCDHj6Q2CG5glXVTULhbmMLQE0YLVFvnik2xGNnlKIJ9GEdsat+PBHR7ToHasGr4JiS7n2uQykxTntgpqB9QNS4fI="),
                List.of(new Pair<>(ISbItem.get(UndeadSword.class).create(), new CoinsCost(100)), new Pair<>(ISbItem.get(EndSword.class).create(), new CoinsCost(150)),
                        new Pair<>(ISbItem.get(SpiderSword.class).create(), new CoinsCost(100)), new Pair<>(ISbItem.get(DiamondSword.class).create(), new CoinsCost(60)),
                        new Pair<>(ISbItem.get(Bow.class).create(), new CoinsCost(25)), new Pair<>(ISbItem.get(FlintArrow.class).create().withAmount(12), new CoinsCost(40)),
                        new Pair<>(ISbItem.get(WitherBow.class).create(), new CoinsCost(250)), new Pair<>(ISbItem.get(ArtisanalShortbow.class).create(), new CoinsCost(600))))};
    }

    @Override
    protected void unregister() {
        foragingReset.cancel();
        spawners.forEach(EntitySpawner::stop);
        crystals.forEach(farmingCrystal -> farmingCrystal.task().cancel());
    }

    public enum Region implements me.carscupcake.sbremake.worlds.region.Region {
        ArcheryRange("§9Archery Range", new Pair<>(new Pos(-12, 67, -130), new Pos(6, 65.22, 155))),
        Graveyard("§cGraveyard", 300, 69, new Pos(-99.0, 75.0, -62.0),new Pos(-92.0, 74.0, -68.0),new Pos(-88.0, 73.0, -81.0),new Pos(-82.0, 74.0, -98.0),new Pos(-77.0, 75.0, -120.0),new Pos(-52.0, 74.0, -136.0),new Pos(-40.0, 80.0, -173.0),new Pos(-41.0, 70.0, -228.0),new Pos(-107.0, 70.0, -217.0),new Pos(-147.0, 70.0, -196.0),new Pos(-178.0, 67.0, -153.0),new Pos(-214.0, 69.0, -96.0),new Pos(-207.0, 77.0, -81.0),new Pos(-173.0, 78.0, -76.0),new Pos(-145.0, 75.0, -77.0),new Pos(-130.0, 80.0, -61.0),new Pos(-108.0, 75.0, -53.0)),
        Forest("§bForest", new Pos(-92.0, 70.0, -18.0), new Pos(-89.0, 70.0, -32.0), new Pos(-92.0, 70.0, -45.0), new Pos(-103.0, 70.0, -51.0), new Pos(-113.0, 75.0, -52.0), new Pos(-122.0, 76.0, -54.0), new Pos(-129.0, 78.0, -59.0), new Pos(-138.0, 80.0, -69.0), new Pos(-147.0, 74.0, -77.0), new Pos(-165.0, 77.0, -75.0), new Pos(-182.0, 77.0, -78.0), new Pos(-206.0, 77.0, -81.0), new Pos(-209.0, 71.0, -77.0), new Pos(-222.0, 65.0, -58.0), new Pos(-225.0, 72.0, -23.0), new Pos(-228.0, 73.0, -6.0), new Pos(-201.0, 74.0, 0.0), new Pos(-176.0, 72.0, 3.0), new Pos(-157.0, 69.0, 16.0), new Pos(-145.0, 70.0, 27.0), new Pos(-134.0, 70.0, 23.0), new Pos(-125.0, 70.0, 17.0), new Pos(-113.0, 70.0, -3.0), new Pos(-100.0, 70.0, -20.0)),
        CoalMine("§bCoal Mine", new Pair<>(new Pos(-36, 0, -231), new Pos(5, 200, -156))),
        HubCrypts("§bCoal Mine", new Pair<>(new Pos(-159, 62, -71), new Pos(-38, 38, -162)));
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
