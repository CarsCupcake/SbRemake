package me.carscupcake.sbremake.command.testing;

import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GcCommand extends Command {
    public GcCommand() {
        super("gc");
        addSyntax((_, _) -> System.gc());
    }
}
