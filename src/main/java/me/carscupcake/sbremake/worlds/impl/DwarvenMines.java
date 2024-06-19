package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
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
}
