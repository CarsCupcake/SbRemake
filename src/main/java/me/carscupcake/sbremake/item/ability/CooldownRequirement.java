package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.utils.time.TimeUnit;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public record CooldownRequirement<T extends PlayerEvent>(long cooldown, TemporalUnit timeUnit) implements Requirement<T> {

    public CooldownRequirement(long seconds) {
        this(seconds, TimeUnit.SECOND);
    }

    public static Map<SkyblockPlayer, Date> cooldowns = new HashMap<>();
    @Override
    public boolean requirement(T t) {
        Date done = cooldowns.get((SkyblockPlayer) t.getPlayer());
        if (done != null) {
            long delta = System.currentTimeMillis() - done.getTime();
            if (delta > 0) {
                cooldowns.remove((SkyblockPlayer) t.getPlayer());
                return true;
            }
            int seconds = (int) ((delta * -1) / 1000d);
            t.getPlayer().sendMessage(STR."§cOn Cooldown for \{seconds}s");
            return false;
        }
        return true;
    }

    @Override
    public void execute(T t) {
        cooldowns.put((SkyblockPlayer) t.getPlayer(), new Date(System.currentTimeMillis() + timeUnit.getDuration().toMillis() * cooldown));
        MinecraftServer.getSchedulerManager().buildTask(() -> cooldowns.remove((SkyblockPlayer) t.getPlayer())).delay(cooldown, ChronoUnit.SECONDS).schedule();
    }
}
