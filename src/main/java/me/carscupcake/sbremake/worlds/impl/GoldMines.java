package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

import java.util.List;

public class GoldMines extends SkyblockWorld.WorldProvider {
    public GoldMines() {
        super(List.of(new Launchpad(-4, -269, -6, -270, 73, SkyblockWorld.Hub, new Pos(-9.5, 65, -228)),
                new Launchpad(-9, -397, -6, -396, 67, SkyblockWorld.DeepCaverns, new Pos(-4, 130, -490))));
        customEntry.put(SkyblockWorld.DeepCaverns, new Pos(-7, 68, -392));
    }

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
