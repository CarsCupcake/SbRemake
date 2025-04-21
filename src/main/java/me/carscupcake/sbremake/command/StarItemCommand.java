package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.item.StarUpgradable;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class StarItemCommand extends Command {
    public StarItemCommand() {
        super("staritem", "star");
        var integerArgument = ArgumentType.Integer("stars").min(0);
        addSyntax((sender, context) -> {
            var stars = context.get(integerArgument);
            var player = (SkyblockPlayer) sender;
            var item = player.getSbItemInHand(Player.Hand.MAIN);
            if (!(item.sbItem() instanceof StarUpgradable starUpgradable)) {
                sender.sendMessage("Your item can not have stars!");
                return;
            }
            stars = starUpgradable.getMaxStars() < stars ? starUpgradable.getMaxStars() : stars;
            player.setItemInHand(Player.Hand.MAIN, item.withModifier(Modifier.STARS, stars).update(player));
        }, integerArgument);
    }
}
