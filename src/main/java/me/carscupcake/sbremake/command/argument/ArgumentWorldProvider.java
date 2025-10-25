package me.carscupcake.sbremake.command.argument;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.command.builder.arguments.ArgumentDynamic;
import net.minestom.server.command.builder.exception.ArgumentSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class ArgumentWorldProvider extends ArgumentDynamic<SkyblockWorld.@NotNull WorldProvider> {
    public ArgumentWorldProvider(String id) {
        super(id, () -> SkyblockWorld.getAllWorlds().stream().map(SkyblockWorld.WorldProvider::getId).toList(), s ->{
            var provider = SkyblockWorld.getAllWorlds().stream().filter(worldProvider -> worldProvider.getId().equals(s)).findFirst().orElse(null);
            if (provider == null) throw new ArgumentSyntaxException("No such world provider!", s, 2);
            return provider;
        });
    }
}
