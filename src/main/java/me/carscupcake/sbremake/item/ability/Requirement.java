package me.carscupcake.sbremake.item.ability;

import net.minestom.server.event.trait.PlayerEvent;

public interface Requirement<T extends PlayerEvent> {
    boolean requirement(T t);
    default void execute(T t) {

    }
}
