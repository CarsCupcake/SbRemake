package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.impl.pets.Pets;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GetPetCommand extends Command {
    public GetPetCommand() {
        super("getpet");
        ArgumentEnum<Pets> petsArgument = new ArgumentEnum<>("pet", Pets.class);
        ArgumentEnum<ItemRarity> rarityArgument = new ArgumentEnum<>("rarity", ItemRarity.class);
        addSyntax((commandSender, commandContext) -> {
            ((SkyblockPlayer) commandSender).addItem(commandContext.get(petsArgument).create(commandContext.get(rarityArgument)).update((SkyblockPlayer) commandSender), false);
        }, petsArgument, rarityArgument);
    }
}
