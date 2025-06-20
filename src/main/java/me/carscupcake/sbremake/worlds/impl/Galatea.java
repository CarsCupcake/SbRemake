package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.world.biome.Biome;

import java.time.Duration;

public class Galatea extends SkyblockWorld.WorldProvider {
    public static final Pos spawnPos = new Pos(-541, 108, -28, 45f, 0f);

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Galatea;
    }

    @Override
    public Pos spawn() {
        return spawnPos;
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        return toMinMaxPair(new Pos(-780, 0, 120), new Pos(-510, 0, -120));
    }

    @Override
    protected void register() {
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            container.getChunks().parallelStream().forEach(chunk -> {
                for (int x = 1; x <= 16; x++)
                    for (int y = 0; y <= 160; y++)
                        for (int z = 1; z <= 16; z++)
                            chunk.setBiome(x, y, z, Biome.MANGROVE_SWAMP);
            });
        }).delay(Duration.ofMillis(500)).schedule();

    }
}
