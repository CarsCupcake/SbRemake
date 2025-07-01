package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.Powder;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.openhft.chronicle.wire.TriConsumer;

public class EssenceCommand extends Command {
    public EssenceCommand() {
        super("essence");
        ArgumentEnum<Essence> powderArg = new ArgumentEnum<>("type", Essence.class);
        ArgumentEnum<Operator> operatorArg = new ArgumentEnum<>("operator", Operator.class);
        ArgumentNumber<Integer> number = new ArgumentInteger("amount").min(0);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(operatorArg).accept(commandContext.get(number), (SkyblockPlayer) commandSender, commandContext.get(powderArg));
        }, operatorArg, powderArg, number);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(operatorArg).accept(-1, (SkyblockPlayer) commandSender, commandContext.get(powderArg));
        }, operatorArg, powderArg);
    }

    public enum Operator implements TriConsumer<Integer, SkyblockPlayer, Essence> {
        add {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Essence powder) {
                if (integer < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.getEssence().add(powder, integer);
            }
        },
        set {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Essence powder) {
                if (integer < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.getEssence().put(powder, integer);
            }
        },
        remove {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Essence powder) {
                if (integer < 0) {
                    player.sendMessage("§cRequires a number");
                    return;
                }
                player.getEssence().subtract(powder, integer);
            }
        },
        reset {
            @Override
            public void accept(Integer integer, SkyblockPlayer player, Essence powder) {
                player.getEssence().put(powder, 0);
            }
        }
    }
}
