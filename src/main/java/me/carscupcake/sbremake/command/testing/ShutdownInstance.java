package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.argument.ArgumentWorldProvider;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;

public class ShutdownInstance extends Command {
    public ShutdownInstance() {
        super("shutdowninstance");
        var argument = new ArgumentWorldProvider("instance");
        addSyntax((commandSender, commandContext) -> {
            var provider = commandContext.get(argument);
            provider.remove();
        }, argument);
    }
}
