package me.carscupcake.sbremake.item.impl.sword;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.Map;

public class EmeraldBlade implements ISbItem, NpcSellable, Listener {
    @Override
    public String getId() {
        return "EMERALD_BLADE";
    }

    @Override
    public String getName() {
        return "Emerald Blade";
    }

    @Override
    public Material getMaterial() {
        return Material.EMERALD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Sword;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public int sellPrice() {
        return 153_000;
    }

    @Override
    public Lore getLore() {
        return new Lore("§7A powerful blade made from pure §2Emeralds§7. This blade becomes stronger as you carry more §6coins §7in your purse§8 (Capped at 2B coins)\n§7\nRecieve §4Curse of Greed §7when striking, §cCANCELLING §7any effect modifying your §6coins §7loss on §4death§7!\n§8 \n§7Current Damage Bonus: §a%b%",
                Map.of("%b%", (_, player) -> StringUtils.cleanDouble(player == null ? 0 : bonus(player))));

    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 130;
        return ISbItem.super.getStat(stat);
    }

    public double bonus(SkyblockPlayer player) {
        return Math.pow(Math.min(player.getCoins(), 2_000_000_000), 0.25) * 2.5;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("emerald.blade").addListener(GetItemStatEvent.class, event -> {
            if (event.getStat() != Stat.Damage || event.getPlayer() == null) return;
            if (event.getItemStack().sbItem().getClass() == EmeraldBlade.class)
                event.setValue(event.getValue() + bonus(event.getPlayer()));
        });
    }
}
