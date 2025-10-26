package me.carscupcake.sbremake.command;

import com.google.common.util.concurrent.Futures;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.config.ConfigFile;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.impl.PrivateIsle;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentLiteral;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Future;

public class VisitCommand extends Command {
    public VisitCommand() {
        super("visit");
        var argumentPlayer = new ArgumentString("playername");
        argumentPlayer.setSuggestionCallback((sender, context, suggestion) -> {
            suggestion.addEntry(new SuggestionEntry("CarsCupcake"));
            suggestion.addEntry(new SuggestionEntry(context.get(argumentPlayer)));
        });
        addSyntax((sender, context) -> {
            String playerName = context.get(argumentPlayer);
            var playerId = SkyblockPlayer.getUUID(playerName);
            visit(playerId, (SkyblockPlayer) sender);
        }, argumentPlayer);
    }

    public void visit(UUID playerId, SkyblockPlayer player) {
        var playerIsles = SkyblockWorld.getWorlds(SkyblockWorld.PrivateIsle);
        var optionalPlayerIsle = playerIsles.stream().filter(worldProvider -> ((PrivateIsle) worldProvider).getOwner().equals(playerId)).findFirst();
        if (optionalPlayerIsle.isPresent()) {
            player.setWorldProvider(optionalPlayerIsle.get());
            return;
        }
        try {
            File dir = new File(ConfigFile.getConfigFolder(playerId), "private_isle");
            if (!dir.exists() || !dir.isDirectory()) {
                player.sendMessage(Component.text("§cPlayer not found!"));
                return;
            }
            var privateIsle = new PrivateIsle(playerId);
            privateIsle.onStart.add(() -> {
                player.setWorldProvider(privateIsle);
            });
            privateIsle.initAsync();
        } catch (Exception e) {
            Main.LOGGER.trace("Could not create isle for player {}", playerId, e);
            player.sendMessage(Component.text("Could not create isle for player " + playerId));
        }
    }
}
