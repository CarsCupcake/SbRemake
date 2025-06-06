package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.Time;
import net.minestom.server.command.builder.Command;
import net.minestom.server.network.packet.server.play.TimeUpdatePacket;

public class UpdateClientTime extends Command {
    public UpdateClientTime() {
        super("updateclinettime");
        addSyntax((sender, context) -> {
            ((SkyblockPlayer) sender).sendPacket(new TimeUpdatePacket(0, Time.tick, false));
        });
    }
}
