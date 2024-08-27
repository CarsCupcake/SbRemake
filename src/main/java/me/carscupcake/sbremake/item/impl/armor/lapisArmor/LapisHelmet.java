package me.carscupcake.sbremake.item.impl.armor.lapisArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;

public class LapisHelmet implements ISbItem, Listener {
    @Override
    public String getId() {
        return "LAPIS_ARMOR_HELMET";
    }

    @Override
    public String getName() {
        return "Lapis Armor Helmet";
    }

    @Override
    public Material getMaterial() {
        return Material.SEA_LANTERN;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Defense) return 25;
        return ISbItem.super.getStat(stat);
    }
    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(HealthFullSetBonus.INSTANCE);
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("item.labis.ability").addListener(PlayerStatEvent.class, event -> {
            if (event.stat() != Stat.Health) return;
            if (!HealthFullSetBonus.players.contains(event.player())) return;
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Health", 60d, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
        });
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }
}
