package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.item.Recipe;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilCommand extends Command {
    public AnvilCommand() {
        super("anvil", "av");
        setDefaultExecutor((sender, ignored) -> {
            Recipe.openAnvilGui((SkyblockPlayer) sender);
        });
    }
}
