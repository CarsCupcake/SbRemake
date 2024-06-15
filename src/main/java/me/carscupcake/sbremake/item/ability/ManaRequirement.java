package me.carscupcake.sbremake.item.ability;

import me.carscupcake.sbremake.command.testing.ToggleCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.event.trait.PlayerEvent;

public record ManaRequirement<T extends PlayerEvent>(long manaCost) implements Requirement<T> {
    @Override
    public boolean requirement(T t) {
        if (ToggleCommand.Toggles.IgnoreMana.toggled) return true;
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
        return player.getMana() >= getManaCost();
    }

    @Override
    public void execute(T t) {
        SkyblockPlayer player = (SkyblockPlayer) t.getPlayer();
        player.setMana(player.getMana() - manaCost);
    }

    public long getManaCost() {
        //Add event
        return manaCost;
    }
}
