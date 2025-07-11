package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.command.testing.ToggleCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.trait.PlayerEvent;

public record SoulflowRequirement<T extends PlayerEvent>(long cost) implements Requirement<T> {
    @Override
    public boolean requirement(T t) {
        if (ToggleCommand.Toggles.IgnoreMana.toggled) return true;
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
        return true; //TODO: Implement
    }

    @Override
    public void execute(T t) {
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
    }
}
