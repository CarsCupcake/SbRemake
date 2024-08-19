package me.carscupcake.sbremake.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HubCommand extends Command {
    public HubCommand() {
        super("hub");
        addSyntax((commandSender, _) -> {
            MinecraftServer.getCommandManager().execute(commandSender, "warp hub");
        });
    }
}
