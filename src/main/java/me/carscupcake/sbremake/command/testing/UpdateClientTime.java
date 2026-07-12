package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.Time;
import net.minestom.server.command.builder.Command;
import net.minestom.server.network.packet.server.play.SetTimePacket;
import net.minestom.server.world.clock.WorldClock;

import java.util.Map;

public class UpdateClientTime extends Command {
    public UpdateClientTime() {
        super("updateclinettime");
        addSyntax((sender, context) -> {
            ((SkyblockPlayer) sender).sendPacket(new SetTimePacket(0, Map.of(WorldClock.OVERWORLD, new SetTimePacket.ClockState(Time.tick, 0, 0))));
        });
    }
}
