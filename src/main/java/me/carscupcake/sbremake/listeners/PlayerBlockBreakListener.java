package me.carscupcake.sbremake.listeners;

import net.minestom.server.event.player.PlayerBlockBreakEvent;

import java.util.function.Consumer;

public class PlayerBlockBreakListener implements Consumer<PlayerBlockBreakEvent> {
    @Override
    public void accept(PlayerBlockBreakEvent playerBlockBreakEvent) {
        playerBlockBreakEvent.setCancelled(true);
    }
}
