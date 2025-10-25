package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.entity.impl.deepCaverns.RedstonePigman;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.impl.other.SkyblockMenu;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.potion.PotionEffect;
import net.minestom.server.event.player.PlayerSpawnEvent;

import java.util.function.Consumer;

public class PlayerSpawnListener implements Consumer<PlayerSpawnEvent> {
    @Override
    public void accept(PlayerSpawnEvent playerSpawnEvent) {
        SkyblockPlayer player = (SkyblockPlayer) playerSpawnEvent.getPlayer();
        if (player.isWarping()) return;
        if (!playerSpawnEvent.isFirstSpawn())
            playerSpawnEvent.getPlayer().spawn();
        player.teleport(player.getWorldProvider().getCustomEntry().getOrDefault(player.getPrevious(), player.getWorldProvider().spawn()));
        RedstonePigman.attacked.remove(player);
        if (playerSpawnEvent.isFirstSpawn()) {
            for (PotionEffect effect : player.getPotionEffects()) {
                player.initPotion(effect);
            }
            player.getPlayerInventory().update();
            player.getPlayerInventory().setItemStack(8, ISbItem.get(SkyblockMenu.class).create());
            player.updateHpBar();
            player.getWorldProvider().addPlayer(player);
            player.spawn();
            System.gc();
        }
    }
}
