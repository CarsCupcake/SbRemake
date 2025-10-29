package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.impl.Hub;
import net.minestom.server.command.builder.Command;

public class CentauriCommand extends Command {
    public CentauriCommand() {
        super("centauri");
        setDefaultExecutor((sender, _) -> {
            Hub.openCentauriInventory((SkyblockPlayer) sender);
        });
    }
}
