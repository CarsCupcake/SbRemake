package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

import java.util.List;

public class SpidersDen extends SkyblockWorld.WorldProvider {
    public SpidersDen() {
        super(List.of(new Launchpad(-199, -230, -195, -226, 82, SkyblockWorld.Hub, new Pos(-160, 75, -156))));
    }

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.SpidersDen;
    }

    @Override
    public Pos spawn() {
        return new Pos(-200.5, 83, -231.5, 135, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }
}
