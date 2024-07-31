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
        System.out.println("got connection");
        SkyblockPlayer player = (SkyblockPlayer) asyncPlayerConfigurationEvent.getPlayer();
        SkyblockWorld world = SkyblockWorld.Hub;
        ConfigFile defaults = new ConfigFile("defaults", player);
        if (defaults.has("world")) {
            world = SkyblockWorld.from(defaults.get("world", ConfigSection.STRING));
        }
        SkyblockWorld.WorldProvider provider = SkyblockWorld.getBestWorld(world);
        if (provider == null) {
            assert world != null;
            provider = world.get();
            SkyblockWorld.WorldProvider finalProvider = provider;
            provider.init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> {
                player.setWorldProvider(finalProvider);
            }, false);
        } else player.setWorldProvider(provider);
        asyncPlayerConfigurationEvent.setSpawningInstance(provider.getContainer());
        asyncPlayerConfigurationEvent.getPlayer().setPermissionLevel(4);
    }
}
