package me.carscupcake.sbremake.item;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

public interface Listener {
    EventNode<Event> node();
}
