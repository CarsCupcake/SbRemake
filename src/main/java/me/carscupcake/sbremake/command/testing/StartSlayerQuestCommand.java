package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.SlayerQuest;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StartSlayerQuestCommand extends Command {
    public StartSlayerQuestCommand() {
        super("slayerquest");
        ArgumentEnum<Slayers> slayer = new ArgumentEnum<>("slayer", Slayers.class);
        ArgumentNumber<Integer> level = new ArgumentInteger("level");
        ArgumentWord word = new ArgumentWord("action");
        word.from("cancle", "claim");
        addSyntax((sender, context) -> {
            ISlayer s = context.get(slayer);
            if (s.startSlayerQuest(context.get(level), (SkyblockPlayer) sender)) return;
            sender.sendMessage("§cYou do not have the requirements!");
        }, slayer, level);
        addSyntax((sender, context) -> {
            SkyblockPlayer player = (SkyblockPlayer) sender;
            if (player.getSlayerQuest() == null) {
                player.sendMessage("§cYou dont have an active slayer quest!");
                return;
            }
            switch (context.get(word)) {
                case "claim" -> {
                    if (player.getSlayerQuest().getStage() != SlayerQuest.SlayerQuestStage.Completed)
                    {
                        player.sendMessage("§cYou did not complete the slayer quest!");
                        return;
                    }
                    player.getSlayerQuest().claim();
                    player.setSlayerQuest(null);

                }
                case "cancle" -> {
                    if (player.getSlayerQuest().getStage() == SlayerQuest.SlayerQuestStage.Completed)
                    {
                        player.sendMessage("§aTo claim your slayer quest use /slayerquest claim");
                        return;
                    }
                    player.setSlayerQuest(null);
                    player.sendMessage("§cCanceled your slayer quest!");
                }
                default -> {
                    player.sendMessage("§cNot a valid option!");
                }
            }
        }, word);
    }
}
