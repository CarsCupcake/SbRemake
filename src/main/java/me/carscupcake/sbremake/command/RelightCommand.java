package me.carscupcake.sbremake.command;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.LightingChunk;

@DebugCommand
public class RelightCommand extends Command {
    public RelightCommand() {
        super("relight");
        setDefaultExecutor((source, args) -> {
            if (source instanceof Player player) {
                long start = System.currentTimeMillis();
                source.sendMessage("Relighting...");
                var relit = LightingChunk.relight(player.getInstance(), player.getInstance().getChunks());
                source.sendMessage("Relighted " + relit.size() + " chunks in " + (System.currentTimeMillis() - start) + "ms");
                relit.forEach(chunk -> chunk.sendChunk(player));
                source.sendMessage("Chunks Received");
            }
        });
    }
}