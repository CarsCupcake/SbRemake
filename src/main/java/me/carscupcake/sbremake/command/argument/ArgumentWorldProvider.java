package me.carscupcake.sbremake.command.argument;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.WorldProvider;
import net.minestom.server.command.builder.arguments.ArgumentDynamic;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import org.jetbrains.annotations.NotNull;

public class ArgumentWorldProvider extends ArgumentDynamic<@NotNull WorldProvider> {
    public ArgumentWorldProvider(String id) {
        super(id, () -> SkyblockWorld.getAllWorlds().stream().map(WorldProvider::getId).toList(), s ->{
            var provider = SkyblockWorld.getAllWorlds().stream().filter(worldProvider -> worldProvider.getId().equals(s)).findFirst().orElse(null);
            if (provider == null) throw new ArgumentSyntaxException("No such world provider!", s, 2);
            return provider;
        });
    }
}
