package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

public class HealthRegenEvent implements PlayerInstanceEvent {

    @Getter
    @Setter
    private double regenAmount;

    private final SkyblockPlayer player;

    public HealthRegenEvent(SkyblockPlayer player, double regenAmount) {
        this.regenAmount = regenAmount;
        this.player = player;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
