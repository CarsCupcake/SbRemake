package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.DungeonItem;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SetItemLevelCommand extends Command {
    public SetItemLevelCommand() {
        super("setitemlevel");
        var intergerArgument = new ArgumentInteger("level").min(0).max(10);
        addSyntax((sender, context) -> {
            var player = (SkyblockPlayer) sender;
            if (!(player.getSbItemInMainHand().sbItem() instanceof DungeonItem)) {
                player.sendMessage("§7You must be holding a dungeon item!");
                return;
            }
            player.setItemInMainHand(player.getSbItemInMainHand().withModifier(DungeonItem.DROPPED_FLOOR, context.get(intergerArgument)).update(player));
        }, intergerArgument);
    }
}
