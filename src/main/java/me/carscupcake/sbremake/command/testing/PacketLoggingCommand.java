package me.carscupcake.sbremake.command.testing;

import me.carscupcake.sbremake.Main;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.player.PlayerPacketEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.ChangeGameStatePacket;
import net.minestom.server.network.packet.server.play.SystemChatPacket;
import net.minestom.server.network.packet.server.play.UnloadChunkPacket;

import java.util.Arrays;

public class PacketLoggingCommand extends Command {
    private  boolean enabled = true;
    public PacketLoggingCommand() {
        super("packetlogging", "pl");

        addSyntax((sender, context) -> {
            enabled = !enabled;
            if (enabled) {
                sender.sendMessage("§aPacket logging enabled");
            } else {
                sender.sendMessage("§cPacket logging disabled");
            }
        });
        MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketEvent.class, (event) -> {
            if (!enabled) return;
            Main.LOGGER.info(STR."RECIEVE \{event.getPacket().getClass().getSimpleName()}");
        });

        MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketOutEvent.class, (event) -> {
            if (!enabled) return;
            if (event.getPacket() instanceof SystemChatPacket) return;
            Main.LOGGER.info(STR."SEND \{event.getPacket().getClass().getSimpleName()}");
            if (event.getPacket() instanceof ChangeGameStatePacket p)
                Main.LOGGER.info("Change game state to " + p.reason() + " " + p.value());
        });
    }
}
