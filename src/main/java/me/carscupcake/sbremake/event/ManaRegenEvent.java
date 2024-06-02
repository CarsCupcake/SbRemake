package me.carscupcake.sbremake.event;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerInstanceEvent;
import org.jetbrains.annotations.NotNull;

public class ManaRegenEvent implements PlayerInstanceEvent {

    @Getter
    @Setter
    private double regenAmount;


    @Getter
    @Setter
    private double multiplier = 1;

    private final SkyblockPlayer player;

    public ManaRegenEvent(SkyblockPlayer player, double regenAmount) {
        this.regenAmount = regenAmount;
        this.player = player;
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }
}
