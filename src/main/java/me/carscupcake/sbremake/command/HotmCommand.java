package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.condition.Conditions;

public class HotmCommand extends Command {

    public HotmCommand() {
        super("hotm");
        addSyntax((commandSender, commandContext) -> {
            ((SkyblockPlayer) commandSender).getHotm().openMenu();
        });
        setCondition(Conditions::playerOnly);
    }
}
