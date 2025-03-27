package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.reforges.Reforge;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReforgeCommand extends Command {
    public ReforgeCommand() {
        super("reforge");
        List<String> reforges = new ArrayList<>(Reforge.reforges.keySet());
        ArgumentWord word = new ArgumentWord("reforgeId").from(reforges.toArray(new String[0]));
        addSyntax((commandSender, commandContext) -> {
            String s = commandContext.get(word);
            if (s == null) return;
            SkyblockPlayer player = (SkyblockPlayer) commandSender;
            SbItemStack item = player.getSbItemInHand(Player.Hand.MAIN);
            if (item == null) {
                player.sendMessage("§cYou do not have a valid item");
                return;
            }
            Reforge reforge = Reforge.reforges.get(s);
            boolean b = false;
            for (ItemType type : reforge.allowedTypes()) {
                if (type == item.sbItem().getType()) {
                    b = true;
                    break;
                }
            }
            if (!b) {
                player.sendMessage("§cNot valid type!");
                return;
            }
            for (Requirement requirement : reforge.requirements())
                if (!requirement.canUse(player, item.item())) {
                    player.sendMessage("§cYou cant use this reforge!");
                    return;
                }
            item = reforge.apply(item);
            player.setItemInHand(Player.Hand.MAIN, item.update(player).item());
        }, word);
    }
}
