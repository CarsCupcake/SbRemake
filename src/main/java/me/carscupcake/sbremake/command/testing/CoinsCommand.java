package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentDouble;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;

import java.util.function.BiConsumer;

public class CoinsCommand extends Command {
    public CoinsCommand() {
        super("coins");
        ArgumentEnum<CoinsCommand.Operator> operatorArg = new ArgumentEnum<>("operator", CoinsCommand.Operator.class);
        ArgumentNumber<Double> number = new ArgumentDouble("amount").min(0d);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(operatorArg).accept(commandContext.get(number), (SkyblockPlayer) commandSender);
        }, operatorArg, number);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(operatorArg).accept(-1d, (SkyblockPlayer) commandSender);
        }, operatorArg);
    }

    public enum Operator implements BiConsumer<Double, SkyblockPlayer> {
        add {
            @Override
            public void accept(Double d, SkyblockPlayer player) {
                if (d < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.addCoins(d);
            }
        },
        set {
            @Override
            public void accept(Double d, SkyblockPlayer player) {
                if (d < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.setCoins(d);
            }
        },
        remove {
            @Override
            public void accept(Double d, SkyblockPlayer player) {
                if (d < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.removeCoins(d);
            }
        },
        reset {
            @Override
            public void accept(Double d, SkyblockPlayer player) {
                player.setCoins(0);
            }
        }
    }
}
