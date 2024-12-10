package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.blocks.impl.EndStone;
import me.carscupcake.sbremake.blocks.impl.Obsidian;
import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;

import java.util.Collections;

public class End extends SkyblockWorld.WorldProvider {
    public End() {
        super(Collections.singletonList(new Launchpad(-496, -277, -495, -274, 100, SkyblockWorld.SpidersDen, new Pos(-378, 130, -261))));
    }
    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.End;
    }

    @Override
    public Pos spawn() {
        return new Pos(-503, 101, -275.5, 90, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    private static final MiningBlock[] ores = {new EndStone(), new Obsidian()};
    @Override
    public MiningBlock[] ores(Pos pos) {
        return ores;
    }
}
