package me.carscupcake.sbremake.util;

import me.carscupcake.sbremake.event.*;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventBinding;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public record PlayerDamageEntityListener(
        Consumer<PlayerToEntityDamageEvent> consumer) implements EventBinding<PlayerToEntityDamageEvent> {
    @Override
    public @NotNull Collection<Class<? extends Event>> eventTypes() {
        return List.of(PlayerToEntityDamageEvent.class, PlayerMeleeDamageEntityEvent.class, PlayerProjectileDamageEntityEvent.class, PlayerDamageEntityEvent.class,
                PlayerToEntityMageDamage.class);
    }

    @Override
    public @NotNull Consumer<@NotNull PlayerToEntityDamageEvent> consumer(@NotNull Class<? extends Event> eventType) {
        return consumer;
    }
}
