package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.particle.Particle;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;
import net.minestom.server.world.biome.BiomeEffects;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public class Galatea extends SkyblockWorld.WorldProvider {
    public static final EventNode<Event> LISTENER = EventNode.all("galatea").addListener(PlayerBlockBreakEvent.class, event -> {
    });
    public static final Pos spawnPos = new Pos(-541, 108, -28, 45f, 0f);

    public static final Biome MOONGLARE = Biome.builder().hasPrecipitation(true).temperature(0.8f).temperatureModifier(Biome.TemperatureModifier.NONE).downfall(0.9f).effects(BiomeEffects.builder().fogColor(new Color(13362894)).waterColor(new Color(3239232)).waterFogColor(new Color(3494160)).skyColor(new Color(13362894)).foliageColor(new Color(6975545)).grassColorModifier(BiomeEffects.GrassColorModifier.SWAMP).biomeParticle(new BiomeEffects.Particle(0.001f, Particle.FALLING_DUST.withBlock(Block.MOSS_BLOCK))).musicVolume(1f).foliageColor(new Color(6975545)).build()).build();
    public static RegistryKey<Biome> MOONGLARE_KEY;

    private final Set<TaskScheduler> taskSchedulers = new HashSet<>();

    public void scheduleTask(Runnable task, int tickDelay) {
        var scheduledTask = new TaskScheduler() {

            @Override
            public void run() {
                task.run();
                taskSchedulers.remove(this);
            }
        };
        scheduledTask.delayTask(tickDelay);
        taskSchedulers.add(scheduledTask);
    }

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
    protected void unregister() {
        super.unregister();
        taskSchedulers.forEach(TaskScheduler::cancel);
        taskSchedulers.clear();
    }

    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        return toMinMaxPair(new Pos(-780, 0, 120), new Pos(-510, 0, -120));
    }

    @Override
    protected void register() {
        MinecraftServer.getSchedulerManager().buildTask(() -> container.getChunks().forEach(chunk -> {
            for (int x = 1; x <= 16; x++)
                for (int y = 0; y <= 160; y++)
                    for (int z = 1; z <= 16; z++)
                        chunk.setBiome(x, y, z, MOONGLARE_KEY);
        })).delay(Duration.ofMillis(500)).schedule();

    }
}
