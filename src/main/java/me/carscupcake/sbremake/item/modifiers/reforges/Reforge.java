package me.carscupcake.sbremake.item.modifiers.reforges;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;

import java.util.*;

public interface Reforge {
    String getName();

    String getId();

    /*
    If it returns Lore.EMPTY its ignored
     */
    Lore getLore();

    double getStat(Stat stat, ItemRarity rarity);

    ItemType[] allowedTypes();

    default List<Requirement> requirements() {
        return new ArrayList<>(0);
    }

    default boolean hasThisReforge(SbItemStack item) {
        return item.getModifier(Modifier.REFORGE) == this;
    }

    default SbItemStack apply(SbItemStack itemStack) {
        return itemStack.applyModifier(Modifier.REFORGE, this);
    }

    Map<String, Reforge> reforges = new HashMap<>();
    EventNode<Event> LISTENER = EventNode.all("reforge").addListener(PlayerMeleeDamageEntityEvent.class, event -> {
        if (!event.isCrit()) return;
        SbItemStack item = SbItemStack.from(event.getPlayer().getItemInHand(Player.Hand.MAIN));
        if (item == null) return;
        if (SwordReforge.Fabled.hasThisReforge(item)) {
            if (new Random().nextDouble() <= 0.075) event.addAdditiveMultiplier(0.15);
        }
    });

    static void init() {
        for (Reforge reforge : SwordReforge.values())
            reforges.put(reforge.getId(), reforge);
        MinecraftServer.getGlobalEventHandler().addChild(LISTENER);
    }
}
