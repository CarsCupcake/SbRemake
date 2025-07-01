package me.carscupcake.sbremake.item.impl.bow;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerProjectileDamageEntityEvent;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

public class WitherBow implements ISbItem, BowItem, Listener, NpcSellable {
    @Override
    public String getId() {
        return "WITHER_BOW";
    }

    @Override
    public String getName() {
        return "Wither Bow";
    }

    @Override
    public Material getMaterial() {
        return Material.BOW;
    }

    @Override
    public ItemType getType() {
        return ItemType.Bow;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §a+100% §7damage to §aWithers §7and §aWither Skeletons§7.");
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 30;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("wither.bow").addListener(PlayerProjectileDamageEntityEvent.class, event -> {
            if (event.getTarget().getEntityType() == EntityType.WITHER || event.getTarget().getEntityType() == EntityType.WITHER_SKELETON) {
                if (event.getProjectile().getItem() != null && event.getProjectile().getItem().sbItem() instanceof WitherBow)
                    event.addAdditiveMultiplier(1);
            }
        });
    }

    @Override
    public int sellPrice() {
        return 1;
    }
}
