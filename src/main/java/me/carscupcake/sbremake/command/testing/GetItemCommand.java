package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.server.command.builder.condition.Conditions;

import java.util.ArrayList;
import java.util.List;

public class GetItemCommand extends Command {
    private static ArgumentWord argument;

    public GetItemCommand() {
        super("getitem");
        List<String> strings = new ArrayList<>(SbItemStack.getIds());
        strings.sort(String::compareTo);
        argument = ArgumentType.Word("id").from(strings.toArray(new String[0]));
        addSyntax(this::execute, argument);
        setDefaultExecutor((source, args) -> source.sendMessage(STR."§cThe item with the id \"\{args.get(argument)}\" was not found"));
        setCondition(Conditions::playerOnly);
    }

    private void execute(CommandSender sender, CommandContext context) {
        SbItemStack item = SbItemStack.from(context.get(argument));
        if (item == null) {
            sender.sendMessage(STR."§cThe item with the id \"\{context.get(argument)}\" was not found");
            return;
        }
        item.update((SkyblockPlayer) sender);
        ((SkyblockPlayer) sender).getInventory().addItemStack(item.item());
    }
}
