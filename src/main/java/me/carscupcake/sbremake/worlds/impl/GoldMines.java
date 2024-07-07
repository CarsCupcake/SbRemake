package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

public class GoldMines extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.GoldMines;
    }

    @Override
    public Pos spawn() {
        return new Pos(-4.5, 74, -272.5, 180, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }
}
