package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.collections.Collection;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentString;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CollectionCommand extends Command {
    public CollectionCommand() {
        super("collectionmenu");
        ArgumentString stringArg = new ArgumentString("collection");
        addSyntax((commandSender, commandContext) -> {
            String id = commandContext.get(stringArg);
            for (Collection collection : ((SkyblockPlayer) commandSender).getCollections())
                if (collection.getId().equalsIgnoreCase(id)) {
                    collection.showInventory();
                    break;
                }
        }, stringArg);
    }
}
