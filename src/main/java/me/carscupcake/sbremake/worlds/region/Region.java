package me.carscupcake.sbremake.worlds.region;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.coordinate.Point;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerMoveEvent;

public interface Region {
    boolean isInRegion(Point pos);

    default void onEnter(SkyblockPlayer player) {
    }

    default void onExit(SkyblockPlayer player) {

    }

    String name();

    EventNode<Event> LISTENER = EventNode.all("region.listener").addListener(PlayerMoveEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        SkyblockWorld.WorldProvider provider = player.getWorldProvider();
        if (provider == null) return;
        for (Region region : provider.regions()) {
            if (region.isInRegion(player.getPosition())) {
                if (player.getRegion() == region) return;
                if (player.getRegion() != null)
                    player.getRegion().onExit(player);
                region.onEnter(player);
                player.setRegion(region);
                return;
            }
        }
        if (player.getRegion() != null) {
            player.getRegion().onExit(player);
            player.setRegion(null);
        }
    });
}
