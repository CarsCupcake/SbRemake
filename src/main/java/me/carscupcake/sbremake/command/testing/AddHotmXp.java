package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddHotmXp extends Command {
    public AddHotmXp() {
        super("hotmxp");
        ArgumentNumber<Integer> number = new ArgumentInteger("amount").min(0);
        addSyntax((commandSender, commandContext) -> {
            ((SkyblockPlayer) commandSender).getHotm().addXp(commandContext.get(number));
        }, number);
    }
}
