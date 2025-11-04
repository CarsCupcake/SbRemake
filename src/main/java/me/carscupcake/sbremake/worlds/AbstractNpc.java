package me.carscupcake.sbremake.worlds;

import lombok.Getter;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.quest.Dialog;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.Instance;

import java.util.HashMap;

@Getter
public abstract class AbstractNpc {
    public static final HashMap<Integer, AbstractNpc> npcs = new HashMap<>();
    private final String name;
    private final Pos pos;
    private final Instance instance;
    private Interaction interaction = null;
    protected String subTag = null;

    public AbstractNpc(Pos pos, Instance instance, String name) {
        this.pos = pos;
        this.instance = instance;
        this.name = name;
    }

    public abstract void spawn(SkyblockPlayer player);

    public abstract int getEntityId();


    public Dialog buildDialog() {
        return new Dialog("§e[NPC] " + (name) + "§f:", 20);
    }

    public AbstractNpc withInteraction(Interaction interaction) {
        this.interaction = interaction;
        return this;
    }

    public interface Interaction {
        void interact(SkyblockPlayer player, PlayerInteractEvent.Interaction interaction);
    }

    public AbstractNpc withClickTag() {
        return withSubTag("§e§lCLICK");
    }

    public AbstractNpc withSubTag(String s) {
        this.subTag = s;
        var stand = new LivingEntity(EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setNoGravity(true);
        stand.setInvulnerable(true);
        stand.set(DataComponents.CUSTOM_NAME, Component.text(s));
        stand.setCustomNameVisible(true);
        stand.setInstance(instance, pos.sub(0, 0.25, 0));
        return this;
    }
}
