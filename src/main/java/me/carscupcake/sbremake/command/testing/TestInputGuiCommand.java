package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.gui.InputGui;
import net.minestom.server.command.builder.Command;

import java.util.List;

@DebugCommand
public class TestInputGuiCommand extends Command {
    public TestInputGuiCommand() {
        super("testinputgui");

        setDefaultExecutor((sender, ignored) -> {
            var inputGui = new InputGui(List.of("", "^^^^^^^^^^^^^^^", "Enter the amount", "to deposit"));
            inputGui.show((SkyblockPlayer) sender, strings -> sender.sendMessage("You entered: " + strings.getFirst()));
        });
    }
}
