package me.carscupcake.sbremake.item.impl.armor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.PlayerDamageEntityListener;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;

public class WardenHelmet implements ISbItem, HeadWithValue, Listener {
    List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Zombie, 8));

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
    public String value() {
        return "eyJ0aW1lc3RhbXAiOjE1ODc4NTE2MTE5NDksInByb2ZpbGVJZCI6ImIwZDczMmZlMDBmNzQwN2U5ZTdmNzQ2MzAxY2Q5OGNhIiwicHJvZmlsZU5hbWUiOiJPUHBscyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTVlYjBiZDg1YWFkZGYwZDI5ZWQwODJlYWMwM2ZjYWRlNDNkMGVlODAzYjBlODE2MmFkZDI4YTYzNzlmYjU0ZSIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19";
    }

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    @Override
    public double getStat(Stat stat) {
        return switch (stat) {
            case Defense -> 100;
            case Health -> 300;
            default -> 0;
        };
    }

    List<Ability> abilities = List.of(BruteForce.instance);

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }

    @Override
    public EventNode<Event> node() {
        var events = EventNode.all("bruteforce_ability").addListener(PlayerStatEvent.class, event -> {
            if (event.stat() != Stat.Speed) return;
            if (event.player().getFullSetBonusPieceAmount(BruteForce.instance) != 1) return;
            event.modifiers().add(new PlayerStatEvent.BasicModifier("Brute Force", 0.5, PlayerStatEvent.Type.MultiplicativeMultiplier, PlayerStatEvent.StatsCategory.Ability));
        });
        events.register(new PlayerDamageEntityListener(event -> {
            if (event.getPlayer().getFullSetBonusPieceAmount(BruteForce.instance) != 1) return;
            var speed = event.getPlayer().getStat(Stat.Speed);
            var mult = 1 + (0.2 * ((int) (speed / 35)));
            event.setWeaponDamage(event.getWeaponDamage() * mult);
        }));
        return events;
    }

    public static class BruteForce extends FullSetBonus {

        private static final BruteForce instance = new BruteForce();
        Lore lore = new Lore("§7Halves your "+Stat.Speed+" §7but grants §c+20% §7base weapon damage for every §a25 "+Stat.Speed+"§7.");

        public BruteForce() {
            super("Brute Force", 1, 1, false);
        }

        @Override
        public void start(SkyblockPlayer player) {

        }

        @Override
        public void stop(SkyblockPlayer player) {

        }

        @Override
        public Lore lore() {
            return lore;
        }
    }
}
