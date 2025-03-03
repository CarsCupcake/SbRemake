package me.carscupcake.sbremake.worlds;

import com.github.f4b6a3.uuid.UuidCreator;
import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.quest.Dialog;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.PlayerInfoUpdatePacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class Npc {
    public static final HashMap<Integer, Npc> npcs = new HashMap<>();
    private Interaction interaction = null;
    private final int entityId;
    private final PlayerInfoUpdatePacket.Entry entry;
    private final Pos pos;
    private final Instance instance;
    public Npc(Pos pos, Instance instance, String name, PlayerSkin playerSkin) {
        entityId = Entity.generateId();
        this.pos = pos;
        this.instance = instance;
        npcs.put(entityId, this);
        PlayerInfoUpdatePacket.Property property = new PlayerInfoUpdatePacket.Property("textures", playerSkin.textures(), playerSkin.signature());
        this.entry = new PlayerInfoUpdatePacket.Entry(UuidCreator.getDceSecurity(UuidCreator.LOCAL_DOMAIN_PERSON, entityId),
                name, List.of(property),
                false, 0, GameMode.CREATIVE, Component.text(name), null, 99);

    }

    public void spawn(SkyblockPlayer player) {
        player.sendPacket(new PlayerInfoUpdatePacket(PlayerInfoUpdatePacket.Action.ADD_PLAYER, entry));
        player.sendPacket(new SpawnEntityPacket(entityId, entry.uuid(), EntityType.PLAYER.id(), getPos(), pos.yaw(), 0, (short) 0, (short) 0, (short) 0));
        byte b = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
        new TaskScheduler() {
            @Override
            public void run() {
                player.sendPacket(new EntityMetaDataPacket(entityId, Map.of(17, Metadata.Byte(b))));
            }
        }.delayTask(10);
    }

    public Npc withInteraction(Interaction interaction) {
        this.interaction = interaction;
        return this;
    }

    public Dialog buildDialog() {
        return new Dialog(STR."§e[NPC] \{StringUtils.stripeColorCodes(entry.username())}§f:", 20);
    }


    public interface Interaction {
        void interact(SkyblockPlayer player, PlayerInteractEvent.Interaction interaction);
    }
    private static char[] chars = {'a', 'a', 'a', 'a', 'a', 'a'};
    private static String makeName() {
        String s = new String(chars);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 'z')
                chars[i] = 'a';
            else {
                chars[i] = chars[i]++;
                break;
            }
        }
        return s;
    }
}
