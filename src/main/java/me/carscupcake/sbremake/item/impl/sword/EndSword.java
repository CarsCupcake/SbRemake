package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

public class EndSword implements ISbItem, Listener, NpcSellable {
    @Override
    public String getId() {
        return "END_SWORD";
    }

    @Override
    public String getName() {
        return "End Sword";
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 35;
        return ISbItem.super.getStat(stat);
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §a+100% §7damage to Endermites, Endermen and Ender Dragons.");
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("end_sword").addListener(PlayerMeleeDamageEntityEvent.class, EndSword::ability);
    }

    private static void ability(@NotNull PlayerMeleeDamageEntityEvent event) {
        if (!(event.getTarget().getEntityType() == EntityType.ENDERMITE || event.getTarget().getEntityType() == EntityType.ENDERMAN || event.getTarget().getEntityType() == EntityType.ENDER_DRAGON))
            return;
        SbItemStack stack = SbItemStack.from(event.getPlayer().getItemInHand(Player.Hand.MAIN));
        if (stack == null) return;
        if (!(stack.sbItem() instanceof EndSword)) return;
        event.addAdditiveMultiplier(1);
    }

    @Override
    public int sellPrice() {
        return 10;
    }
}
