package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PetsMenuCommand extends Command {
    public PetsMenuCommand() {
        super("petsmenu", "petmenu", "pets");
        setDefaultExecutor((commandSender, _) -> {
            ((SkyblockPlayer) commandSender).openPetsMenu();
        });
    }
}
