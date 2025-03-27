package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.command.builder.arguments.number.ArgumentNumber;
import net.minestom.server.command.builder.condition.Conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetItemCommand extends Command {
    private static ArgumentWord argument;
    private static ArgumentNumber<Integer> num;

    public GetItemCommand() {
        super("getitem");
        List<String> strings = new ArrayList<>(SbItemStack.getIds());
        strings.sort(String::compareTo);
        argument = ArgumentType.Word("id").from(strings.toArray(new String[0]));
        num = new ArgumentInteger("amount").min(1);
        addSyntax(this::execute, argument);
        addSyntax(this::execute, argument, num);
        setDefaultExecutor((source, args) -> source.sendMessage("§cThe item with the id " +(args.get(argument)) + " was not found"));
        setCondition(Conditions::playerOnly);
    }

    private void execute(CommandSender sender, CommandContext context) {
        var player = (SkyblockPlayer) sender;
        SbItemStack item = SbItemStack.from(context.get(argument));
        if (item == null) {
            sender.sendMessage("§cThe item with the id " + (context.get(argument)) + " was not found");
            return;
        }
        item.update((SkyblockPlayer) sender);
        var maxStackSize = Objects.requireNonNull(item.withAmount(item.sbItem().getMaxStackSize()));
        var amount = context.getOrDefault(num, 1);
        while (amount > maxStackSize.item().amount()){
            if (!player.addItem(maxStackSize)) return;
            amount -= maxStackSize.item().amount();
        }
        if (amount > 0) {
            player.addItem(item.withAmount(amount));
        }
    }
}
