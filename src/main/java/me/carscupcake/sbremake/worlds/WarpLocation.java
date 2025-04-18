package me.carscupcake.sbremake.worlds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.carscupcake.sbremake.worlds.impl.CrimsonIsle;
import net.minestom.server.coordinate.Pos;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Getter
public enum WarpLocation {
    Hub("hub", SkyblockWorld.Hub, new Pos(-2.5, 70.5, -70.5, 180, 0)),
    PrivateIsle("home", SkyblockWorld.PrivateIsle, new Pos(7.5, 100, 7.5)),
    GoldMines("gold", SkyblockWorld.GoldMines, new Pos(-4.5, 74, -272.5, 180, 0)),
    DeepCaverns("deep", SkyblockWorld.DeepCaverns, new Pos(4, 157, 85, 180, 0)),
    DwarvenMines("mines", SkyblockWorld.DwarvenMines, new Pos(-48.5, 200, -121.5, 270, 0)),
    Park("park", SkyblockWorld.Park, new Pos(-276.5, 82.5, -13.5, 90, 0)),
    Barn("barn", SkyblockWorld.FarmingIsles, new Pos(113.5, 71, -207.5, -135, 0)),
    MushroomDesert("desert", SkyblockWorld.FarmingIsles, new Pos(160.5, 77, -370, -147, 0)),
    SpidersDen("spider", SkyblockWorld.SpidersDen, new Pos(-200.5, 83, -231.5, 135, 0)),
    SpidersDenTopOfTheNest("top", SkyblockWorld.SpidersDen, new Pos(-189, 176, -310, 43.5f, 0)),
    End("end", SkyblockWorld.End, new Pos(-503, 101.5, -275.5, 90, 0)),
    CrimsonIsle("crimson_isle", SkyblockWorld.CrimsonIsle, me.carscupcake.sbremake.worlds.impl.CrimsonIsle.SPAWN_POS, "nether", "crimson") {};
    private final String id;
    private final SkyblockWorld world;
    private final Pos spawn;
    private String[] alias = new String[0];

    WarpLocation(String id, SkyblockWorld world, Pos spawn, String... alias) {
        this(id, world, spawn);
        this.alias = alias;
    }

    public static @Nullable WarpLocation fromId(String id) {
        for (WarpLocation location : values()) {
            if (location.id.equals(id)) {
                return location;
            }
            for (String alias : location.getAlias()) {
                if (alias.equals(id)) {
                    return location;
                }
            }
        }
        return null;
    }
}
