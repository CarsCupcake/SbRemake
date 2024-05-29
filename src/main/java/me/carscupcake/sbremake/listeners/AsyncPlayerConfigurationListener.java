package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.permission.Permission;

import java.util.function.Consumer;

public class AsyncPlayerConfigurationListener implements Consumer<AsyncPlayerConfigurationEvent> {
    @Override
    public void accept(AsyncPlayerConfigurationEvent asyncPlayerConfigurationEvent) {
         ((SkyblockPlayer) asyncPlayerConfigurationEvent.getPlayer()).configure();
        SkyblockWorld.WorldProvider provider = SkyblockWorld.getBestWorld(SkyblockWorld.Hub);
        asyncPlayerConfigurationEvent.setSpawningInstance(provider.getContainer());
        ((SkyblockPlayer) asyncPlayerConfigurationEvent.getPlayer()).setWorldProvider(provider);
        asyncPlayerConfigurationEvent.getPlayer().setPermissionLevel(4);
    }
}
