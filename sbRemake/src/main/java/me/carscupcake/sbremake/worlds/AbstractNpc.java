package me.carscupcake.sbremake.worlds;

import lombok.Getter;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.quest.Dialog;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

import java.util.HashMap;

@Getter
public abstract class AbstractNpc {
    public static final HashMap<Integer, AbstractNpc> npcs = new HashMap<>();
    private final String name;
    private final Pos pos;
    private final Instance instance;
    private Interaction interaction = null;

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
}
