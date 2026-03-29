package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

@DebugCommand
public class ChunkAtCommand extends Command {
    public ChunkAtCommand() {
        super("chunkat");
        setDefaultExecutor((sender, _) -> {
            var player = (SkyblockPlayer) sender;
            player.sendMessage("Chunk: " + player.getPosition().chunkX() + " " + player.getPosition().chunkZ());
            player.sendMessage("Chunk Location: " + (player.getPosition().blockX() % 16) + " " + player.getPosition().y() + " " + (player.getPosition().blockZ() % 16));
        });
    }
}
