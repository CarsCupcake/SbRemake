package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.function.Consumer;

public class PlayerSpawnListener implements Consumer<PlayerSpawnEvent> {
    @Override
    public void accept(PlayerSpawnEvent playerSpawnEvent) {
        playerSpawnEvent.getPlayer().spawn();
        SkyblockPlayer player = (SkyblockPlayer) playerSpawnEvent.getPlayer();
        player.teleport(player.getWorldProvider().spawn());
        if (playerSpawnEvent.isFirstSpawn()) {
            ConfigFile file = new ConfigFile("inventory", player);
            if (file.getRawElement() == null) return;
            if (file.getRawElement().getAsJsonObject().isEmpty()) return;
            player.getInventory().clear();
            for (int i = 0; i < player.getInventory().getSize(); i++) {
                if (file.has(STR."\{i}"))
                    player.getInventory().setItemStack(i, file.get(STR."\{i}", ConfigSection.ITEM).update(player).item());
            }
        }
    }
}
