package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.WarpLocation;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.condition.Conditions;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand extends Command {
    private ArgumentWord word;

    public WarpCommand() {
        super("warp");
        setCondition(Conditions::playerOnly);
        List<String> strings = new ArrayList<>();
        for (WarpLocation world : WarpLocation.values())
            strings.add(world.getId());
        strings.sort(String::compareTo);
        word = ArgumentType.Word("warp").from(strings.toArray(new String[0]));
        addSyntax((commandSender, commandContext) -> {
            String id = commandContext.get(word);
            WarpLocation world = WarpLocation.fromId(id);
            assert world != null;
            SkyblockPlayer player = (SkyblockPlayer) commandSender;
            if (player.isOnLaunchpad()) {
                player.sendMessage("§cYou cant do this on Launchpad!");
                return;
            }
            if (world.getWorld() == player.getWorldProvider().type()) {
                player.teleport(world.getSpawn());
                player.playSound(SoundType.ENTITY_ENDERMAN_TELEPORT.create(1, 1));
                return;
            }
            SkyblockWorld.sendToBest(world, player);
        }, word);
    }
}
