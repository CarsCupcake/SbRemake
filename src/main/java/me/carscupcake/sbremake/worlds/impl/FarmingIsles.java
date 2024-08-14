package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

public class FarmingIsles extends SkyblockWorld.WorldProvider {
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
        return new Region[0];
    }
}
