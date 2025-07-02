package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;

public class ItemData extends Command {
    public ItemData() {
        super("itemdata");
        addSyntax((commandSender, _) -> {
            System.out.println(((SkyblockPlayer) commandSender).getSbItemInHand(PlayerHand.MAIN).item().toItemNBT());
        });
    }


}
