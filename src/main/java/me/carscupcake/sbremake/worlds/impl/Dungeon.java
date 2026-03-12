package me.carscupcake.sbremake.worlds.impl;

import lombok.Getter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.util.Pos2d;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.WorldProvider;
import me.carscupcake.sbremake.worlds.impl.dungeon.DungeonWorldProvider;
import me.carscupcake.sbremake.worlds.impl.dungeon.Generator;
import me.carscupcake.sbremake.worlds.impl.dungeon.Paster;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.IChunkLoader;

import java.io.IOException;

public class Dungeon extends WorldProvider {

    @Getter
    private final Generator generator;
    private final Pos spawn;
    @Getter
    private final int floor;
    private boolean isDungeonLoaded = false;

    public Dungeon(Generator generator, int floor) {
        this.generator = generator;
        spawn = new Pos(15, 71, generator.getEntrance().pos().z() * 32 + 15);
        this.floor = floor;
        Main.LOGGER.debug("Dungeon Spawn: {}", spawn);
    }

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.Dungeon;
    }

    @Override
    public IChunkLoader getChunkLoader() throws IOException {
        generator.determineIds();
        return new DungeonWorldProvider(this, generator, generator.getRoomIds());
    }

    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        var max = getMaxPos(floor);
        return new Pair<>(new Pos(-31, 0,-31), new Pos(Math.max(generator.getRooms().length * 31, max.blockX()), 0, generator.getRooms()[0].length * 31 + 2 + max.blockZ()));
    }

    @Override
    public Pos spawn() {
        return spawn;
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    private static Pos getMaxPos(int floor) {
        return switch (floor) {
            case 1 -> Pos.ZERO; //new Pos(60, 0, 161);
            default -> throw new IllegalArgumentException("Invalid floor: " + floor);
        };
    }

    @Override
    public boolean onPlayerAdd(SkyblockPlayer player) {
        if (!isDungeonLoaded) {
            isDungeonLoaded = true;
            Paster.paste(new Pos2d(0, generator.getRooms().length), "assets/schematics/dungeon/boss/floor" + floor + ".schematic.gz", getContainer());
        }
        return super.onPlayerAdd(player);
    }
}
