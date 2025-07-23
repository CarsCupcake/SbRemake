package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.item.StarUpgradable;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShowUpgradeGuiCommand extends Command {
    public ShowUpgradeGuiCommand() {
        super("upgradeitem");
        setDefaultExecutor((sender, ignored) -> StarUpgradable.showStarUpgradingGui((SkyblockPlayer) sender));
    }
}
