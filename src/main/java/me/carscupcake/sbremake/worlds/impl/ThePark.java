package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

public class ThePark extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Park;
    }

    @Override
    public Pos spawn() {
        return new Pos(-276.5, 82.5, -13.5, 90, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }
}
