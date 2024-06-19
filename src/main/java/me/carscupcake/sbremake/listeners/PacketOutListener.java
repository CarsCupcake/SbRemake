package me.carscupcake.sbremake.listeners;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.attribute.AttributeInstance;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.network.packet.server.play.EntityAttributesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerAbilitiesPacket;

import java.util.List;
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
        if (event.getPacket() instanceof EntityAttributesPacket(int entityId, List<AttributeInstance> properties)) {
            if (entityId != event.getPlayer().getEntityId()) return;
            for (AttributeInstance attributeInstance : properties)
                if (attributeInstance.getAttribute() == Attribute.GENERIC_ATTACK_SPEED && attributeInstance.getValue() != 4d) {
                    attributeInstance.getModifiers().clear();
                    attributeInstance.setBaseValue(4);
                }
        }
        /*if (event.getPacket() instanceof PlayerAbilitiesPacket packet) {
            if (packet.walkingSpeed() <= 0.2f) return;
            event.setCancelled(true);
            System.out.println("ARG UwU");
            PlayerAbilitiesPacket playerAbilitiesPacket = new PlayerAbilitiesPacket(packet.flags(), packet.flyingSpeed(), 0.2f);
            event.getPlayer().sendPacket(playerAbilitiesPacket);
        }*/
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
