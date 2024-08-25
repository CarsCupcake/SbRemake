package me.carscupcake.sbremake.item.impl.armor.dragon.superior;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class SuperiorDragonChestplate implements ISbItem, ISbItem.StatProvider, NpcSellable, ColoredLeather {
    @Override
    public String getId() {
        return "SUPERIOR_DRAGON_CHESTPLATE";
    }

    @Override
    public String getName() {
        return "Superior Dragon Chestplate";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public ItemType getType() {
        return ItemType.Chestplate;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 150, Stat.Defense, 190, Stat.Speed, 3, Stat.Strength, 10, Stat.Intelligence, 25, Stat.CritChance, 2, Stat.CritDamage, 10);
    }

    @Override
    public int sellPrice() {
        return 100_000;
    }

    @Override
    public Color color() {
        return new Color(0xf2df11);
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SkillRequirement(Skill.Combat, 20));
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(SuperiorBloodFullSetBonus.INSTANCE);
    }
}
