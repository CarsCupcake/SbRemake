package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.item.ItemFilter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class ItemsCommand extends Command {
    public ItemsCommand() {
        super("items");
        setDefaultExecutor((sender, _) -> {
            ItemFilter.openGui((SkyblockPlayer) sender);
        });
    }
}
