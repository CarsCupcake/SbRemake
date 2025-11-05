package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class ShardsCommand extends Command {
    public ShardsCommand() {
        super("shards");
        setDefaultExecutor((sender, _) -> ((SkyblockPlayer) sender).getAttributeMenu().openHuntingBox());
    }
}
