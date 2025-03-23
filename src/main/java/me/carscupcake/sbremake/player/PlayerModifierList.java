package me.carscupcake.sbremake.player;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.Pair;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;

import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Consumer;

public class PlayerModifierList {
    private final MapList<Stat, Pair<PlayerStatEvent.PlayerStatModifier, Task>> modifiers = new MapList<>();
    public void add(Stat stat, PlayerStatEvent.PlayerStatModifier modifier, Duration duration) {
        Pair<PlayerStatEvent.PlayerStatModifier, Task> pre = null;
        for (Pair<PlayerStatEvent.PlayerStatModifier, Task> pair : modifiers.get(stat)) {
            if (pair.getFirst().equals(modifier)) {
                pair.getSecond().cancel();
                pre = pair;
                break;
            }
        }
        if (pre != null) modifiers.removeFromList(stat, pre);
        Pair<PlayerStatEvent.PlayerStatModifier, Task> taskPair = new Pair<>(modifier, MinecraftServer.getSchedulerManager().buildTask(() -> removeModifier(stat, modifier)).delay(duration).schedule());
        modifiers.add(stat, taskPair);
    }

    public void forEachModifier(Stat stat, Consumer<PlayerStatEvent.PlayerStatModifier> consumer) {
        for (Pair<PlayerStatEvent.PlayerStatModifier, Task> pair : modifiers.get(stat))
            consumer.accept(pair.getFirst());
    }

    public void removeModifier(Stat stat, PlayerStatEvent.PlayerStatModifier modifier) {
        for (Pair<PlayerStatEvent.PlayerStatModifier, Task> pair : new ArrayList<>(modifiers.get(stat)))
            if (pair.getFirst().equals(modifier)) {
                modifiers.removeFromList(stat, pair);
                pair.getSecond().cancel();
            }
    }
}
