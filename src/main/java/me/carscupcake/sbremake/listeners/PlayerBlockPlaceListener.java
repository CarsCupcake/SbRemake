package me.carscupcake.sbremake.listeners;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;

import java.util.function.Consumer;

public class PlayerBlockPlaceListener implements Consumer<PlayerBlockPlaceEvent> {
    @Override
    public void accept(PlayerBlockPlaceEvent playerBlockPlaceEvent) {
        playerBlockPlaceEvent.setCancelled(true);
    }
}
