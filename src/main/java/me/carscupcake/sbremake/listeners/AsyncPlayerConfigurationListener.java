package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;

import java.util.function.Consumer;

public class AsyncPlayerConfigurationListener implements Consumer<AsyncPlayerConfigurationEvent> {
    @Override
    public void accept(AsyncPlayerConfigurationEvent asyncPlayerConfigurationEvent) {
        SkyblockPlayer player = (SkyblockPlayer) asyncPlayerConfigurationEvent.getPlayer();
        if (player.getWorldProvider() != null) player.setWorldProvider(null);
        SkyblockWorld world = SkyblockWorld.PrivateIsle;
        ConfigFile defaults = new ConfigFile("defaults", player);
        if (defaults.has("world")) {
            world = SkyblockWorld.from(defaults.get("world", ConfigSection.STRING));
        }
        assert world != null;
        SkyblockWorld.WorldProvider provider = SkyblockWorld.getBestWorld(player, world);
        if (provider == null) {
            provider = world.get();
        }
        if (!provider.isLoaded()) {
            SkyblockWorld.WorldProvider finalProvider = provider;
            provider.init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> player.setWorldProvider(finalProvider), false);
        } else {
            player.setWorldProvider(provider);
        }
        asyncPlayerConfigurationEvent.setSpawningInstance(provider.getContainer());
        asyncPlayerConfigurationEvent.getPlayer().setPermissionLevel(4);
    }
}
