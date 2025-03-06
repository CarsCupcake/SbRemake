package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.command.builder.Command;

import java.util.List;
import java.util.Map;

public class InstancesCommand extends Command {
    public InstancesCommand() {
        super("instances");
        setDefaultExecutor((sender, _) -> {
            if (SkyblockWorld.getWorlds().isEmpty()) {
                sender.sendMessage("There are no skyblock worlds");
                return;
            }

            sender.sendMessage("§9--------------------------------------------------");
            for (Map.Entry<SkyblockWorld, List<SkyblockWorld.WorldProvider>> entry : SkyblockWorld.getWorlds().entrySet()) {
                sender.sendMessage("§a%s§7:".formatted(entry.getKey().name()));
                for (SkyblockWorld.WorldProvider world : entry.getValue()) {
                    sender.sendMessage("§7- §a%s %d Players".formatted(world.getId(), world.getPlayers().size()));
                }
            }
            sender.sendMessage("§9--------------------------------------------------");
        });
    }
}
