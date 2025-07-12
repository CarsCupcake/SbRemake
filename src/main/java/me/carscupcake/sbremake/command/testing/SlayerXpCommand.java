package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.openhft.chronicle.wire.TriConsumer;

public class SlayerXpCommand extends Command {
    public SlayerXpCommand() {
        super("slayerxp");
        ArgumentEnum<Slayers> powderArg = new ArgumentEnum<>("type", Slayers.class);
        ArgumentEnum<SlayerXpCommand.Operator> operatorArg = new ArgumentEnum<>("operator", SlayerXpCommand.Operator.class);
        ArgumentNumber<Integer> number = new ArgumentInteger("amount").min(0);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(operatorArg).accept(commandContext.get(number), (SkyblockPlayer) commandSender, commandContext.get(powderArg));
        }, operatorArg, powderArg, number);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(operatorArg).accept(-1, (SkyblockPlayer) commandSender, commandContext.get(powderArg));
        }, operatorArg, powderArg);

    }

    public enum Operator implements TriConsumer<Integer, SkyblockPlayer, Slayers> {
        add {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Slayers powder) {
                if (integer < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.getSlayers().get(powder).addXp(integer);
            }
        }, set {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Slayers powder) {
                if (integer < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.getSlayers().get(powder).setXp(integer);
            }
        }, remove {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Slayers powder) {
                if (integer < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.getSlayers().get(powder).subtractXp(integer);
            }
        }, reset {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Slayers powder) {
                player.getSlayers().get(powder).resetXp();
            }
        }
    }
}
