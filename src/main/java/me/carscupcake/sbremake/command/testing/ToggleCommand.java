package me.carscupcake.sbremake.command.testing;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import org.jetbrains.annotations.NotNull;

public class ToggleCommand extends Command {

    private final ArgumentEnum<@NotNull Toggles> toggles;

    public ToggleCommand() {
        super("toggle");
        toggles = new ArgumentEnum<>("toggles", Toggles.class);
        addSyntax((ignored, commandContext) -> {
            commandContext.get(toggles).toggle();
        }, toggles);
    }

    public enum Toggles {
        IgnoreMana, IgnoreCooldown;
        public boolean toggled = false;

        public void toggle() {
            toggled = !toggled;
        }
    }
}
