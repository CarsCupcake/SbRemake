package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.impl.sword.slayer.zombie.RevenantFalchion;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

public class UndeadSword implements ISbItem, Listener, NpcSellable {
    @Override
    public String getId() {
        return "UNDEAD_SWORD";
    }

    @Override
    public String getName() {
        return "Undead Sword";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 30;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §a+100% §7damage to Zombie Pigmen, Skeletons, Whithers and Zombies.");
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("undead_sword").addListener(PlayerMeleeDamageEntityEvent.class, event -> {
            if (!(event.getTarget().getEntityType() == EntityType.ZOMBIFIED_PIGLIN || event.getTarget().getEntityType() == EntityType.SKELETON || event.getTarget().getEntityType() == EntityType.WITHER || event.getTarget().getEntityType() == EntityType.ZOMBIE))
                return;
            SbItemStack stack = event.getPlayer().getSbItemInHand(Player.Hand.MAIN);
            if (stack == null) return;
            if ((stack.sbItem() instanceof UndeadSword)) {
                event.addAdditiveMultiplier(1);
            } else if (stack.sbItem() instanceof RevenantFalchion) {
                if (stack.sbItem().requirements().getFirst().canUse(event.getPlayer(), stack.item())) {
                    event.addAdditiveMultiplier(1.5);
                }
            }
        });
    }

    @Override
    public int sellPrice() {
        return 5;
    }
}
