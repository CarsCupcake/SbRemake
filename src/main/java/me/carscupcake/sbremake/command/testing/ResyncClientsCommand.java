package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;

import java.util.Objects;

public class ResyncClientsCommand extends Command {

    public ResyncClientsCommand() {
        super("resyncpos");
        var playerArgument = new ArgumentString("player");
        setDefaultExecutor((sender, context) -> {
            var player = (SkyblockPlayer) sender;
            var instance = player.getInstance();
            Objects.requireNonNull(instance.getChunk(player.getPosition().chunkX(), player.getPosition().chunkZ())).sendChunk(player);
            player.synchronizeNextTick();
        });
    }
}
