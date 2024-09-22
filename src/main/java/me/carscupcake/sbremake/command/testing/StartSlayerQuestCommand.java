package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StartSlayerQuestCommand extends Command {
    public StartSlayerQuestCommand() {
        super("slayerquest");
        ArgumentEnum<Slayers> slayer = new ArgumentEnum<>("slayer", Slayers.class);
        ArgumentNumber<Integer> level = new ArgumentInteger("level");
        addSyntax((sender, context) -> {
            ISlayer s = context.get(slayer);
            if (s.startSlayerQuest(context.get(level), (SkyblockPlayer) sender)) return;
            sender.sendMessage("§cYou do not have the requirements!");
        }, slayer, level);
    }
}
