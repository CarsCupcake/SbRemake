package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddAbsorbtionHeartsCommand extends Command {
    public AddAbsorbtionHeartsCommand() {
        super("absorption");
        ArgumentNumber<Double> number = new ArgumentDouble("amount").min(0d);
        addSyntax((commandSender, commandContext) -> {
            ((SkyblockPlayer) commandSender).addAbsorption(commandContext.get(number));
        }, number);
    }
}
