package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.command.testing.ToggleCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.trait.PlayerEvent;
import net.minestom.server.utils.time.TimeUnit;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.Map;

public record CooldownRequirement<T extends PlayerEvent>(long cooldown,
                                                         ChronoUnit timeUnit) implements Requirement<T> {

    public CooldownRequirement(double seconds) {
        this((long) (seconds * 1000), ChronoUnit.MILLIS);
    }

    public static Map<SkyblockPlayer, Long> cooldowns = new HashMap<>();

    @Override
    public boolean requirement(T t) {
        if (ToggleCommand.Toggles.IgnoreCooldown.toggled) return true;
        Long done = cooldowns.get((SkyblockPlayer) t.getPlayer());
        if (done != null) {
            long delta = System.currentTimeMillis() - done;
            if (delta > 0) {
                cooldowns.remove((SkyblockPlayer) t.getPlayer());
                return true;
            }
            int seconds = (int) ((delta * -1) / 1000d);
            t.getPlayer().sendMessage("§cThe ability is on Cooldown for " + (seconds) + "s.");
            return false;
        }
        return true;
    }

    @Override
    public void execute(T t) {
        if (ToggleCommand.Toggles.IgnoreCooldown.toggled) return;
        cooldowns.put((SkyblockPlayer) t.getPlayer(), System.currentTimeMillis() + timeUnit.getDuration().toMillis() * cooldown);
        MinecraftServer.getSchedulerManager().buildTask(() -> cooldowns.remove((SkyblockPlayer) t.getPlayer())).delay(cooldown, timeUnit).schedule();
    }
}
