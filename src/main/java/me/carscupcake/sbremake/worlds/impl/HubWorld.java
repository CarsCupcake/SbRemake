package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.coordinate.Pos;

public class HubWorld extends SkyblockWorld.WorldProvider {
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Hub;
    }

    @Override
    public Pos spawn() {
        return new Pos(-2.5, 70.5, -70.5, 180, 0);
    }
}
