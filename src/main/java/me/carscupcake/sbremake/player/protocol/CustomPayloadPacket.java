package me.carscupcake.sbremake.player.protocol;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.ServerPacket;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public record CustomPayloadPacket(CustomPayload customPayload) implements ServerPacket.Play {

    //24
    @Override
    public int playId() {
        return 24;
    }

    @Override
    public void write(@NotNull NetworkBuffer networkBuffer) {
        customPayload.write(networkBuffer);
    }

    public interface CustomPayload {
        void write(@NotNull NetworkBuffer networkBuffer);
    }
}
