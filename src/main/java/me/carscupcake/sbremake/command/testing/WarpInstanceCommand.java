package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.argument.ArgumentWorldProvider;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentDynamic;

public class WarpInstanceCommand extends Command {
    public WarpInstanceCommand() {
        super("warpinstance");
        var argument = new ArgumentWorldProvider("instance");

        addSyntax((sender, context) -> {
            var worldProvider = context.get(argument);
            var player = (SkyblockPlayer) sender;
            if (player.getWorldProvider() == worldProvider) {
                sender.sendMessage("§cYou are already in this instance!");
                return;
            }
            player.setWorldProvider(worldProvider);
        }, argument);
    }


}
