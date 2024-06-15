package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;

import java.util.Map;
import java.util.function.Consumer;

public class PacketOutListener implements Consumer<PlayerPacketOutEvent> {
    @Override
    public void accept(PlayerPacketOutEvent event) {
        if (event.getPacket() instanceof EntityMetaDataPacket(int entityId, Map<Integer, Metadata.Entry<?>> entries)) {
            Entity entity = event.getPlayer().getInstance().getEntities().stream().filter(entity1 -> entity1.getEntityId() == entityId).findFirst().orElse(null);
            if (entity == null || entity.getEntityType() != EntityType.PLAYER) return;
            //Checking if its bow drawing or if it even is legal
            Metadata.Entry<?> metadata = entries.get(8);
            //Type 0 is byte
            if (metadata == null || metadata.type() != 0) return;
            byte b = (byte) metadata.value();
            //1 stands for Is hand active (https://wiki.vg/Entity_metadata#Entity_Metadata_Format)
            if (b != 1) return;
            //Bow draw or eating animation is triggered
            SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
            if (player.getBowStartPull() < 0 && player.getItemInHand(Player.Hand.MAIN).material() == Material.BOW)
                event.setCancelled(true);
        }
    }

    private static float getHealth(double maxHealth) {
        float health = 0;
        if (maxHealth < 125) {
            health = 20;
        } else if (maxHealth < 165) {
            health = 22;
        } else if (maxHealth < 230) {
            health = 24;
        } else if (maxHealth < 300) {
            health = 26;
        } else if (maxHealth < 400) {
            health = 28;
        } else if (maxHealth < 500) {
            health = 30;
        } else if (maxHealth < 650) {
            health = 32;
        } else if (maxHealth < 800) {
            health = 34;
        } else if (maxHealth < 1000) {
            health = 36;
        } else if (maxHealth < 1250) {
            health = 38;
        } else if (maxHealth >= 1250) {
            health = 40;
        }
        return health;
    }
}
