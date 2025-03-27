package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.command.builder.condition.Conditions;

public class SetHealthCommand extends Command {
    private static final ArgumentFloat argument = ArgumentType.Float("amount");

    public SetHealthCommand() {
        super("sethealth");

        setCondition(Conditions::playerOnly);
        addSyntax(this::execute, argument);

    }

    private void execute(CommandSender sender, CommandContext context) {
        SkyblockPlayer player = (SkyblockPlayer) sender;
        player.setHealth(context.get(argument));
    }
}
