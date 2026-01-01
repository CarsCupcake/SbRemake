package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.shard.IAttributeShard;
import me.carscupcake.sbremake.item.impl.shard.Shard;
import me.carscupcake.sbremake.item.impl.shard.ShardItem;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentWord;

import java.util.Arrays;

public class GetShardItemCommand extends Command {
    public GetShardItemCommand() {
        super("getsharditem", "getshard");
        var argument = new ArgumentWord("id").from(Arrays.stream(Shard.values()).map(Shard::getId).toArray(String[]::new));
        addSyntax((sender, context) -> {
            var id = context.get(argument);
            var player = (SkyblockPlayer) sender;
            player.addItem(SbItemStack.from(ShardItem.class).withModifier(Modifier.ATTRIBUTE, IAttributeShard.fromKey(id)).update(player));
        }, argument);
    }
}
