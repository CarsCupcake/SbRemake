package me.carscupcake.sbremake.item.impl.armor.dungeon;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.EssenceCost;
import net.minestom.server.item.Material;

import java.util.Map;

public class WitherGoggles implements ISbItem, Dungeonizable, HeadWithValue, ISbItem.StatProvider {

    private final Cost[][] costs = new Cost[][] {
            { new EssenceCost(Essence.Undead, 40) },
            { new EssenceCost(Essence.Undead, 80) },
            { new EssenceCost(Essence.Undead, 150) },
            { new EssenceCost(Essence.Undead, 300), new CoinsCost(10_000) },
            { new EssenceCost(Essence.Undead, 600), new CoinsCost(25_000) }
    };

    @Override
    public Cost[] upgradeCost(SbItemStack item, int star) {
        return costs[star];
    }

    @Override
    public Essence getEssence() {
        return Essence.Undead;
    }

    @Override
    public String getId() {
        return "WITHER_GOGGLES";
    }

    @Override
    public String getName() {
        return "Wither Goggles";
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
        return ItemRarity.EPIC;
    }

    @Override
    public int getMaxStars() {
        return 5;
    }

    @Override
    public boolean isDungeonItem() {
        return true;
    }

    @Override
    public String value() {
        return "ewogICJ0aW1lc3RhbXAiIDogMTYwNTU0MzM0MTg4MSwKICAicHJvZmlsZUlkIiA6ICJiMGQ0YjI4YmMxZDc0ODg5YWYwZTg2NjFjZWU5NmFhYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJNaW5lU2tpbl9vcmciLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzdjZWI4ZjA3NThlMmQ4YWM0OWRlNmY5Nzc2MDNjN2JmYzIzZmQ4MmE4NTc0ODEwYTQ1ZjVlOTdjNjQzNmQ3OSIKICAgIH0KICB9Cn0=";
    }

    Map<Stat, Number> stats = Map.of(Stat.Intelligence, 300, Stat.AbilityDamage, 45);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }
}
