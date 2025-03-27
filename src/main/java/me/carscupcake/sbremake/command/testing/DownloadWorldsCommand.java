package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DownloadWorldsCommand extends Command {
    public DownloadWorldsCommand() {
        super("downloadworlds");
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Downloading worlds...");
            for (SkyblockWorld world : SkyblockWorld.values()) {
                try {
                    world.updateFiles();
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            }
        });
    }
}
