package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

public class DwarvenMines extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.DwarvenMines;
    }

    @Override
    public Pos spawn() {
        return new Pos(-48.5, 200, -121.5, 270, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        return toMinMaxPair(new Pos(-235, 0, 300), new Pos(300, 0, -200));
    }
}
