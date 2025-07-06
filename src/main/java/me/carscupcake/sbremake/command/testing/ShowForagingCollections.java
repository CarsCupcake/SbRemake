package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.item.collections.CollectionTypes;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

@DebugCommand
public class ShowForagingCollections extends Command {
    public ShowForagingCollections() {
        super("showforagingcollections");
        setDefaultExecutor((sender, _) -> {
            var player = (SkyblockPlayer) sender;
            CollectionTypes.Foraging.openMenu(player);
        });
    }
}
