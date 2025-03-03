package me.carscupcake.sbremake.item.impl.bow;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.*;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Terminator implements ISbItem, Shortbow, ISbItem.StatProvider, Listener {
    @Override
    public String getId() {
        return "TERMINATOR";
    }

    @Override
    public String getName() {
        return "Terminator";
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
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 310d, Stat.Strength, 50d, Stat.CritChance, 10d, Stat.CritDamage, 250d, Stat.AttackSpeed, 40d);
    }

    @Override
    public int arrowsToShoot(SbItemStack stack) {
        return 3;
    }

    @Override
    public Lore getLore() {
        return new Lore(List.of("§7Shoots §b3 §7arrows at once.", "§7Can damage endermen.", "§c  ", STR."§cDivides your \{Stat.CritChance.toString()} §cby 4!"));
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("terminator.ability").addListener(PlayerStatEvent.class, playerStatEvent -> {
            if (playerStatEvent.stat() != Stat.CritChance) return;
            SbItemStack item = SbItemStack.from(playerStatEvent.player().getItemInHand(PlayerHand.MAIN));
            if (item != null && item.sbItem() instanceof Terminator)
                playerStatEvent.modifiers().add(new PlayerStatEvent.BasicModifier("Terminator", 0.25, PlayerStatEvent.Type.MultiplicativeMultiplier, PlayerStatEvent.StatsCategory.Armor));
        });
    }
}