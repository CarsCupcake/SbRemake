package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

public class CrimsonIsle extends SkyblockWorld.WorldProvider {
    public static final Pos SPAWN_POS = new Pos(-360.5, 80.1, -430.5, 180f, 0f);
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.CrimsonIsle;
    }

    @Override
    public Pos spawn() {
        return SPAWN_POS;
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }
}
