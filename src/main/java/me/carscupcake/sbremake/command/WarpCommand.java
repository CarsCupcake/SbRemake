package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand extends Command {
    private ArgumentWord word;

    public WarpCommand() {
        super("warp");
        List<String> strings = new ArrayList<>();
        for (SkyblockWorld world : SkyblockWorld.values())
            strings.add(world.getId());
        strings.sort(String::compareTo);
        word = ArgumentType.Word("warp").from(strings.toArray(new String[0]));
        addSyntax((commandSender, commandContext) -> {
            String id = commandContext.get(word);
            SkyblockWorld world = SkyblockWorld.from(id);
            assert world != null;
            SkyblockWorld.WorldProvider provider = SkyblockWorld.getBestWorld(world);
            if (provider == null) {
                provider = world.get();
                SkyblockWorld.WorldProvider finalProvider = provider;
                provider.init(MinecraftServer.getInstanceManager().createInstanceContainer(), () -> ((SkyblockPlayer) commandSender).setWorldProvider(finalProvider));
            } else ((SkyblockPlayer) commandSender).setWorldProvider(provider);
        }, word);
    }
}
