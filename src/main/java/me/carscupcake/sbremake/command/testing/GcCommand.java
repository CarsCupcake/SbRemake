package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import net.minestom.server.command.builder.Command;

@DebugCommand
public class GcCommand extends Command {
    public GcCommand() {
        super("gc");
        addSyntax((ignored, ignored2) -> System.gc());
    }
}
