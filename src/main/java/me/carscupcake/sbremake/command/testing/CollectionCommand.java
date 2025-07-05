package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;

public class CollectionCommand extends Command {
    public CollectionCommand() {
        super("collectionmenu");
        ArgumentString stringArg = new ArgumentString("collection");
        addSyntax((commandSender, commandContext) -> {
            String id = commandContext.get(stringArg);
            var collection = ((SkyblockPlayer) commandSender).getCollections().get(id);
            if (collection == null) {
                commandSender.sendMessage("No such collection exists!");
            } else {
                collection.showInventory();
            }
        }, stringArg);
    }
}
