package me.carscupcake.sbremake.command.testing;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;

public class ToggleCommand extends Command {

    private final ArgumentEnum<Toggles> toggles;

    public ToggleCommand() {
        super("toggle");
        toggles = new ArgumentEnum<>("toggles", Toggles.class);
        addSyntax((_, commandContext) -> {
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
