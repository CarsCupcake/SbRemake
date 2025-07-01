package me.carscupcake.sbremake.item.impl.armor.dragon.superior;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.color.Color;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class SuperiorDragonLeggings implements ISbItem, ISbItem.StatProvider, NpcSellable, ColoredLeather, GemstoneSlots {
    @Override
    public String getId() {
        return "SUPERIOR_DRAGON_LEGGINGS";
    }

    @Override
    public String getName() {
        return "Superior Dragon Leggings";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_LEGGINGS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Leggings;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 130, Stat.Defense, 170, Stat.Speed, 3, Stat.Strength, 10, Stat.Intelligence, 25, Stat.CritChance, 2, Stat.CritDamage, 10);
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

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Combat};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{false};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[][]{{new CoinsCost(50_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Fine).asItem(), 5),
                new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Sapphire).get(Gemstone.Quality.Fine).asItem(), 5),
                new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Ruby).get(Gemstone.Quality.Fine).asItem(), 5),
                new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Amethyst).get(Gemstone.Quality.Fine).asItem(), 5)}};
    }
}
