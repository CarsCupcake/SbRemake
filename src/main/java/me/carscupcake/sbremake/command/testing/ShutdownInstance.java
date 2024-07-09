package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShutdownInstance extends Command {
    public ShutdownInstance() {
        super("shutdowninstance");
        ArgumentString string = new ArgumentString("worldid");
        addSyntax((commandSender, commandContext) -> {
            String id = commandContext.get(string);
            for (SkyblockWorld.WorldProvider provider : SkyblockWorld.getAllWorlds()) {
                if (provider.getId().equals(id)) {
                    provider.remove();
                    return;
                }
            }
        }, string);
    }
}
