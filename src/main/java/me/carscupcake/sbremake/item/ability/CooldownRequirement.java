package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.trait.PlayerEvent;

import java.time.temporal.ChronoUnit;
import java.util.Vector;

public record CooldownRequirement<T extends PlayerEvent>(long cooldownSeconds) implements Requirement<T> {
    public static Vector<SkyblockPlayer> cooldowns = new Vector<>();
    @Override
    public boolean requirement(T t) {
        return !cooldowns.contains((SkyblockPlayer) t.getPlayer());
    }

    @Override
    public void execute(T t) {
        cooldowns.add((SkyblockPlayer) t.getPlayer());
        MinecraftServer.getSchedulerManager().buildTask(() -> cooldowns.remove((SkyblockPlayer) t.getPlayer())).delay(cooldownSeconds, ChronoUnit.SECONDS).schedule();
    }
}
