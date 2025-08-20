package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@DebugCommand
public class TestUpdateListing extends Command {
    public TestUpdateListing() {
        super("testlisting");
        setDefaultExecutor((sender, context) -> {
            var player = (SkyblockPlayer) sender;
            for (int i = 0; i < 40; i++) {
               // player.getWidgetContainer().updateRow(i, Component.text("Listing " + i));
            }
        });
    }
}
