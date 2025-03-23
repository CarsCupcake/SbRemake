package me.carscupcake.sbremake.item.impl.armor.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class WardenHelmet implements ISbItem, ISbItem.StatProvider, HeadWithValue, Listener {
    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWViNTY0NDQzMjZiNWY0NjE0NDkwMzk3NTJmMjYyMjdhMjRiMDY3OWFjMDc3OGFjNjkwYTU2Zjk2YTBjMjFjYSJ9fX0=";
    }

    @Override
    public String getId() {
        return "WARDEN_HELMET";
    }

    @Override
    public String getName() {
        return "Warden Helmet";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 300, Stat.Defense, 100);
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SlayerRequirement(Slayers.Zombie, 8));
    }

    @Override
    public Lore getLore() {
        return new Lore("§6Ability: Brute Force\n§7Halves your " + (Stat.Speed) + " §7but grants §c+20% §7base weapon damage for every §a25 " + (Stat.Speed) + "§7.");
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("item.warden_helmet").addListener(PlayerStatEvent.class, event -> {
            if (event.stat() == Stat.Damage) {
                SbItemStack helmet = SbItemStack.from(event.getPlayer().getHelmet());
                if (helmet == null) return;
                if (helmet.sbItem() instanceof WardenHelmet && hasRequirements(event.player(), helmet.item())) {
                    int i = ((int) (event.player().getStat(Stat.Speed) / 25d));
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Brute Force", (i * 0.2), PlayerStatEvent.Type.AddativeMultiplier, PlayerStatEvent.StatsCategory.Ability));
                }
            }
            if (event.stat() == Stat.Speed) {
                SbItemStack helmet = SbItemStack.from(event.getPlayer().getHelmet());
                if (helmet == null) return;
                if (helmet.sbItem() instanceof WardenHelmet && hasRequirements(event.player(), helmet.item())) {
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Brute Force", 0.5, PlayerStatEvent.Type.MultiplicativeMultiplier, PlayerStatEvent.StatsCategory.Ability));
                }
            }
        });
    }
}
