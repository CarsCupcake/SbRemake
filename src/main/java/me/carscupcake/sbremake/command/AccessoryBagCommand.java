package me.carscupcake.sbremake.command;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AccessoryBagCommand extends Command {
    public AccessoryBagCommand() {
        super("accessorybag", "accessories");
        setDefaultExecutor((sender, ignored) -> {
            ((SkyblockPlayer) sender).getAccessoryBag().openInventory((SkyblockPlayer) sender);
        });
    }
}
