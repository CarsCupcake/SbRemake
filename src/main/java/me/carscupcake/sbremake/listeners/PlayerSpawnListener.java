package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.function.Consumer;

public class PlayerSpawnListener implements Consumer<PlayerSpawnEvent> {
    @Override
    public void accept(PlayerSpawnEvent playerSpawnEvent) {
        playerSpawnEvent.getPlayer().spawn();
        SkyblockPlayer player = (SkyblockPlayer) playerSpawnEvent.getPlayer();
        player.teleport(player.getWorldProvider().spawn());
    }
}
