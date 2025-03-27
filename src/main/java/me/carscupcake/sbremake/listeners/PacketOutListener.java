package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeModifier;
import net.minestom.server.entity.attribute.AttributeOperation;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.EntityAttributesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerAbilitiesPacket;
import net.minestom.server.utils.NamespaceID;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class PacketOutListener implements Consumer<PlayerPacketOutEvent> {
    @Override
    public void accept(PlayerPacketOutEvent event) {
        if (event.getPacket() instanceof EntityMetaDataPacket(int entityId, Map<Integer, Metadata.Entry<?>> entries)) {
            if (event.getPlayer().getInstance() == null) return;
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
            if (player.getBowStartPull() < 0 && player.getSbItemInHand(Player.Hand.MAIN).item().material() == Material.BOW)
                event.setCancelled(true);
        }
        if (event.getPacket() instanceof EntityAttributesPacket(
                int entityId, List<EntityAttributesPacket.Property> properties
        )) {
            if (entityId != event.getPlayer().getEntityId()) return;
            for (EntityAttributesPacket.Property attributeInstance : properties)
                if (attributeInstance.attribute() == Attribute.GENERIC_ATTACK_SPEED && attributeInstance.value() != 4d) {
                    attributeInstance.modifiers().clear();
                    attributeInstance.modifiers().add(new AttributeModifier(NamespaceID.from("base"), 4, AttributeOperation.ADD_VALUE));
                }

        if (event.getPacket() instanceof PlayerAbilitiesPacket(_, _, float speed)) {
            if (speed != 0.1) event.setCancelled(true);
        }
        }
    }

}
