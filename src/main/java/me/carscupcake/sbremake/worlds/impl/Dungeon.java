package me.carscupcake.sbremake.worlds.impl;

import lombok.Getter;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.WorldProvider;
import me.carscupcake.sbremake.worlds.impl.dungeon.DungeonWorldProvider;
import me.carscupcake.sbremake.worlds.impl.dungeon.Generator;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.IChunkLoader;

import java.io.IOException;

public class Dungeon extends WorldProvider {

    @Getter
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
        generator.determineIds();
        return new DungeonWorldProvider(generator, generator.getRoomIds());
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
