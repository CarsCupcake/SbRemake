package me.carscupcake.sbremake.command.testing;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToggleCommand extends Command {

    private final ArgumentEnum<Toggles> toggles;

    public ToggleCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);
        toggles = new ArgumentEnum<>("toggles", Toggles.class);
        addSyntax((commandSender, commandContext) -> {
            commandContext.get(toggles).toggle();
        }, toggles);
    }

    public enum Toggles {
        IgnoreMana,
        IgnoreCooldown;
        public boolean toggled = false;
        public void toggle() {
            toggled = !toggled;
        }
    }
}
