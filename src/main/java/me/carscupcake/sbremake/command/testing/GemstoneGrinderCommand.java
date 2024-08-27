package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GemstoneGrinderCommand extends Command {
    public GemstoneGrinderCommand() {
        super("gemstonegrinder", "grinder");
        addSyntax((commandSender, commandContext) -> {
            Gemstone.openGemstoneGrinder((SkyblockPlayer) commandSender);
        });
    }
}
