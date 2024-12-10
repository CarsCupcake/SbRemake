package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;
//A the current stage i dotn have the world downloaded :/
public class End extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.End;
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
