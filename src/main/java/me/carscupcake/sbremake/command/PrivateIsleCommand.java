package me.carscupcake.sbremake.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

public class PrivateIsleCommand extends Command {
    public PrivateIsleCommand() {
        super("is");
        addSyntax((sender, _) -> {
            MinecraftServer.getCommandManager().execute(sender, "warp home");
        });
    }
}
