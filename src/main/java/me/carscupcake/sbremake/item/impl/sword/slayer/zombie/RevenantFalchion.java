package me.carscupcake.sbremake.item.impl.sword.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class RevenantFalchion implements ISbItem, ISbItem.StatProvider {
    @Override
    public String getId() {
        return "REVENANT_SWORD";
    }

    @Override
    public String getName() {
        return "Revenant Falchion";
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
        return ItemRarity.RARE;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 90, Stat.Strength, 50, Stat.Intelligence, 100);
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SlayerRequirement(Slayers.Zombie, 3));
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §a+150% §7damage to Zombies.");
    }
}
