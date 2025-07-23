package me.carscupcake.sbremake.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

public class HubCommand extends Command {
    public HubCommand() {
        super("hub");
        addSyntax((commandSender, ignored) -> {
            MinecraftServer.getCommandManager().execute(commandSender, "warp hub");
        });
    }
}
