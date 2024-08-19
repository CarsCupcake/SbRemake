package me.carscupcake.sbremake.worlds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.coordinate.Pos;

import javax.annotation.Nullable;

@RequiredArgsConstructor
@Getter
public enum WarpLocation {
    Hub("hub", SkyblockWorld.Hub, new Pos(-2.5, 70.5, -70.5, 180, 0)),
    GoldMines("gold", SkyblockWorld.GoldMines, new Pos(-4.5, 74, -272.5, 180, 0)),
    DeepCaverns("deep", SkyblockWorld.DeepCaverns, new Pos(4, 157, 85, 180, 0)),
    DwarvenMines("mines", SkyblockWorld.DwarvenMines, new Pos(-48.5, 200, -121.5, 270, 0)),
    Park("park", SkyblockWorld.Park, new Pos(-276.5, 82.5, -13.5, 90, 0)),
    Barn("barn", SkyblockWorld.FarmingIsles, new Pos(113.5, 71, -207.5, -135, 0)),
    MushroomDesert("desert", SkyblockWorld.FarmingIsles, new Pos(160.5, 77, -370, -147, 0)),
    SpidersDen("spider", SkyblockWorld.SpidersDen, new Pos(-200.5, 83, -231.5, 135, 0)),
    SpidersDenTopOfTheNest("top", SkyblockWorld.SpidersDen, new Pos(-189, 176, -310, 43.5f, 0));
    private final String id;
    private final SkyblockWorld world;
    private final Pos spawn;

    public static @Nullable WarpLocation fromId(String id) {
        for (WarpLocation location : values())
            if (location.id.equals(id)) return location;
        return null;
    }
}
