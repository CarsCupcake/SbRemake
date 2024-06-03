package me.carscupcake.sbremake.listeners;

import net.minestom.server.event.player.PlayerPacketEvent;

import java.util.function.Consumer;

public class PacketInListener implements Consumer<PlayerPacketEvent> {
    @Override
    public void accept(PlayerPacketEvent playerPacketEvent) {
        System.out.println(playerPacketEvent.getPacket().getClass().getSimpleName());
    }
}
