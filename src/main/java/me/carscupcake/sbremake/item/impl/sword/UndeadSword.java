package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.impl.sword.slayer.zombie.RevenantFalchion;
import me.carscupcake.sbremake.util.ArrayUtil;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
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
        return new Lore("§7Deals §a+100% §7damage to " + MobType.Undead + " §7Mobs.");
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("undead_sword").addListener(PlayerMeleeDamageEntityEvent.class, event -> {
            if (!ArrayUtil.contains(event.getTarget().getMobTypes(), MobType.Undead))
                return;
            SbItemStack stack = event.getPlayer().getSbItemInHand(PlayerHand.MAIN);
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
