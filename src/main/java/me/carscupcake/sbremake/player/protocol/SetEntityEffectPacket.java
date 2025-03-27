package me.carscupcake.sbremake.player.protocol;

import net.minestom.server.entity.Entity;
import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.network.packet.server.ServerPacket;
import net.minestom.server.network.packet.server.ServerPacketIdentifier;
import net.minestom.server.potion.Potion;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "UnstableApiUsage"})
public record SetEntityEffectPacket(int entity, int effectId, int amplifier, int duration,
                                    byte flags) implements ServerPacket.Play {

    public SetEntityEffectPacket(Entity entity, Potion effect) {
        this(entity.getEntityId(), effect.effect().id(), effect.amplifier(), effect.duration(), effect.flags());
    }

    @Override
    public int playId() {
        return ServerPacketIdentifier.ENTITY_EFFECT;
    }

    @Override
    public void write(@NotNull NetworkBuffer networkBuffer) {
        networkBuffer.write(NetworkBuffer.VAR_INT, entity);
        networkBuffer.write(NetworkBuffer.VAR_INT, effectId);
        networkBuffer.write(NetworkBuffer.VAR_INT, amplifier);
        networkBuffer.write(NetworkBuffer.VAR_INT, duration);
        networkBuffer.write(NetworkBuffer.BYTE, flags);
    }
}
