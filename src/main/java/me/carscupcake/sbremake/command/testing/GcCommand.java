package me.carscupcake.sbremake.command.testing;

import net.minestom.server.command.builder.Command;

public class GcCommand extends Command {
    public GcCommand() {
        super("gc");
        addSyntax((_, _) -> System.gc());
    }
}
