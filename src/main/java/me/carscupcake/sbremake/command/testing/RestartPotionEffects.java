package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.command.DebugCommand;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.command.builder.Command;

@DebugCommand
public class RestartPotionEffects extends Command {
    public RestartPotionEffects() {
        super("restartpotioneffects");
        setDefaultExecutor((sender, command) -> {
            var player = (SkyblockPlayer)  sender;
            for (var effect : player.getPotionEffects())
                effect.potion().start(player, effect.amplifier(), (long) ((effect.expiration() - System.currentTimeMillis()) / 50d));
        });
    }
}
