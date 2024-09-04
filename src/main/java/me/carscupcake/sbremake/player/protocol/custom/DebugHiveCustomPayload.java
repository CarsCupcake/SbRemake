package me.carscupcake.sbremake.player.protocol.custom;

import me.carscupcake.sbremake.player.protocol.CustomPayloadPacket;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.network.NetworkBuffer;
import org.jetbrains.annotations.NotNull;

public record DebugHiveCustomPayload(HiveInfo hiveInfo) implements CustomPayloadPacket.CustomPayload {
    @Override
    public void write(@NotNull NetworkBuffer buf) {
        buf.write(NetworkBuffer.BLOCK_POSITION, hiveInfo().blockVec);
        buf.write(NetworkBuffer.STRING, hiveInfo.hiveType);
        buf.write(NetworkBuffer.INT, hiveInfo.occupantCount);
        buf.write(NetworkBuffer.INT, hiveInfo.honeyLevel);
        buf.write(NetworkBuffer.BOOLEAN, hiveInfo.sedated);
    }

    public record HiveInfo(BlockVec blockVec, String hiveType, int occupantCount, int honeyLevel, boolean sedated) {
    }
}
