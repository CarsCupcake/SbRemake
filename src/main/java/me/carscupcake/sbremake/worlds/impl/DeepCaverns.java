package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

public class DeepCaverns extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.DeepCaverns;
    }

    @Override
    public Pos spawn() {
        return new Pos(4, 157, 85, 180, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }
}
