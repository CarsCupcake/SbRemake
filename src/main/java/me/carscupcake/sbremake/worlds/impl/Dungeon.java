package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.impl.dungeon.DungeonWorldProvider;
import me.carscupcake.sbremake.worlds.impl.dungeon.Generator;
import me.carscupcake.sbremake.worlds.impl.dungeon.Paster;
import me.carscupcake.sbremake.worlds.impl.dungeon.Room;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.instance.InstanceContainer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class Dungeon extends SkyblockWorld.WorldProvider {

    private final Generator generator;

    public Dungeon(Generator generator) {
        this.generator = generator;
    }

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Dungeon;
    }

    @Override
    public IChunkLoader getChunkLoader() throws IOException {
        return new DungeonWorldProvider(generator, new String[6][6]);
    }

    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        return new Pair<>(Pos.ZERO, new Pos(6*31, 0, 6*31));
    }

    @Override
    public Pos spawn() {
        return new Pos(0, 140, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }
}
