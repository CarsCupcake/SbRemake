package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class BankCommand extends Command {
    public BankCommand() {
        super("personalbank", "bank");
        setDefaultExecutor((sender, ignored) -> ((SkyblockPlayer) sender).showBank());
    }
}
