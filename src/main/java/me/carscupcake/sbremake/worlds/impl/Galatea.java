package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

public class Galatea extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Galatea;
    }

    @Override
    public Pos spawn() {
        return null;
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }
}
