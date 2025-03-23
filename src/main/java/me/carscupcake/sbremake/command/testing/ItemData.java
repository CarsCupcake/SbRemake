package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class ItemData extends Command {
    public ItemData() {
        super("itemdata");
        addSyntax((commandSender, _) -> {
            System.out.println(((SkyblockPlayer) commandSender).getItemInHand(Player.Hand.MAIN).toItemNBT());
        });
    }


}
