package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

public class AttributeMenuCommand extends Command {
    public AttributeMenuCommand() {
        super("attributemenu", "attributes");
        setDefaultExecutor((sender, _) -> ((SkyblockPlayer) sender).getAttributeMenu().open());
    }
}
