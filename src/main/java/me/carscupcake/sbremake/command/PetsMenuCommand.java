package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class PetsMenuCommand extends Command {
    public PetsMenuCommand() {
        super("petsmenu", "petmenu", "pets");
        setDefaultExecutor((commandSender, ignored) -> {
            ((SkyblockPlayer) commandSender).openPetsMenu();
        });
    }
}
