package me.carscupcake.sbremake.worlds;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.ComponentUtil;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.quest.Dialog;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.color.Color;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Setter
@Getter
public class Npc extends AbstractNpc {
    private final int entityId;
    private final PlayerInfoUpdatePacket.Entry entry;
    private final String fakeName = makeName();
    private final LivingEntity nameStand;

    public Npc(Pos pos, Instance instance, String name, PlayerSkin playerSkin) {
        super(pos, instance, name);
        entityId = Entity.generateId();
        PlayerInfoUpdatePacket.Property property = new PlayerInfoUpdatePacket.Property("textures", playerSkin.textures(), playerSkin.signature());
        this.entry = new PlayerInfoUpdatePacket.Entry(UuidCreator.getDceSecurity(UuidCreator.LOCAL_DOMAIN_PERSON, entityId),
                fakeName, List.of(property),
                false, 0, GameMode.CREATIVE, ComponentUtil.compile(name), null, 0, true);
        this.nameStand = new LivingEntity(EntityType.ARMOR_STAND);
        nameStand.setInvisible(true);
        nameStand.setCustomNameVisible(true);
        nameStand.setInstance(instance, pos);
        nameStand.setNoGravity(true);
        nameStand.setInvulnerable(true);
        nameStand.set(DataComponents.CUSTOM_NAME, Component.text(name));
    }

    public void spawn(SkyblockPlayer player) {
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));
        player.sendPacket(new SpawnEntityPacket(entityId, entry.uuid(), EntityType.PLAYER.id(), getPos(), getPos().yaw(), 0, (short) 0, (short) 0, (short) 0));
        byte b = 0b1111111;
        new TaskScheduler() {
            @Override
            public void run() {
                player.sendPacket(new EntityMetaDataPacket(entityId, Map.of(17, Metadata.Byte(b))));
                //player.sendPacket(new PlayerInfoRemovePacket(entry.uuid()));
                player.sendPacket(new TeamsPacket(fakeName, new TeamsPacket.CreateTeamAction(Component.text(fakeName), (byte) 0, TeamsPacket.NameTagVisibility.NEVER, TeamsPacket.CollisionRule.NEVER, NamedTextColor.BLACK, Component.empty(), Component.empty(), List.of(fakeName))));
            }
        }.delayTask(10);
    }

    public Dialog buildDialog() {
        return new Dialog("§e[NPC] " + (StringUtils.stripeColorCodes(entry.username())) + "§f:", 20);
    }

    private static final Random random = new Random();

    private static String makeName() {
        var builder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            var rand = random.nextInt(36);
            if (rand > 25) {
                builder.append((rand - 26));
            } else
                builder.append((char) ('a' + rand));
        }
        Main.LOGGER.debug("Generated fake name {}", builder.toString());
        return builder.toString();
    }

}
