package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.Main;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A simple shutdown command.
 */
public class ShutdownCommand extends Command {

    public ShutdownCommand() {
        super("shutdown");
        addSyntax(this::execute);
    }

    @Override
    public @Nullable String[] getAliases() {
        return new String[]{"stop"};
    }

    private void execute(@NotNull CommandSender commandSender, @NotNull CommandContext commandContext) {
        Main.running.set(false);
        MinecraftServer.stopCleanly();
    }
}
