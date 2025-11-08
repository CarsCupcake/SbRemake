package me.carscupcake.sbremake.event.eventBinding;

import me.carscupcake.sbremake.event.EntityDamagePlayerEvent;
import me.carscupcake.sbremake.event.EntityMeleeDamagePlayerEvent;
import me.carscupcake.sbremake.event.ProjectileDamagePlayerEvent;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventBinding;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public record EntityToPlayerDamageBinding(
        Consumer<EntityDamagePlayerEvent> eventConsumer) implements EventBinding<@NotNull EntityDamagePlayerEvent> {

    private static final Collection<Class<? extends Event>> eventTypes = List.of(EntityMeleeDamagePlayerEvent.class,
            ProjectileDamagePlayerEvent.class);

    @Override
    public @NotNull Collection<Class<? extends Event>> eventTypes() {
        return eventTypes;
    }

    @Override
    public @NotNull Consumer<@NotNull EntityDamagePlayerEvent> consumer(@NotNull Class<? extends Event> eventType) {
        return eventConsumer;
    }
}
