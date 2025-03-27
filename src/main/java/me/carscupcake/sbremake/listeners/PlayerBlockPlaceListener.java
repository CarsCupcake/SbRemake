package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;

import java.util.function.Consumer;

public class PlayerBlockPlaceListener implements Consumer<PlayerBlockPlaceEvent> {
    @Override
    public void accept(PlayerBlockPlaceEvent playerBlockPlaceEvent) {
        if (((SkyblockPlayer) playerBlockPlaceEvent.getPlayer()).getWorldProvider().type() == SkyblockWorld.PrivateIsle)
            return;
        playerBlockPlaceEvent.setCancelled(true);
    }
}
