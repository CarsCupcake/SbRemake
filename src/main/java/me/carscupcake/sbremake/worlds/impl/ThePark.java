package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;

import java.util.HashMap;

public class ThePark extends ForagingIsle {

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.ThePark;
    }

    @Override
    public Pos spawn() {
        return new Pos(-263.5, 79, -17.5, 90, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }


    @Override
    public boolean useCustomMining() {
        return false;
    }
    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        return new  Pair<>(new Pos(0, 0, 0), new Pos(0, 0, 0));
    }
}
