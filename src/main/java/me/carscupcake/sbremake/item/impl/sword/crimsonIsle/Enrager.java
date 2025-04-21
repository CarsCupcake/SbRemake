package me.carscupcake.sbremake.item.impl.sword.crimsonIsle;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.ability.*;
import me.carscupcake.sbremake.item.impl.other.crimsonIsle.BlazeAshes;
import me.carscupcake.sbremake.item.impl.other.crimsonIsle.HeavyPearl;
import me.carscupcake.sbremake.item.impl.other.crimsonIsle.MutatedBLazeAshes;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.EssenceCost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class Enrager implements ISbItem, ISbItem.StatProvider, GemstoneSlots, NpcSellable, StarUpgradable {
    private static final List<Ability> abilities = List.of(new ItemAbility<>("Enrage", AbilityType.RIGHT_CLICK, _ -> {
        //TODO: Implement
    }, new Lore("§7Taunt enemies in a §a10 block§7 radius and reduce their " + Stat.Damage + " against you by §c10%§7 for §a10s§7."), new ManaRequirement<>(500), new HealthRequirement<>(0.1)));
    @Override
    public String getId() {
        return "ENRAGER";
    }

    @Override
    public String getName() {
        return "Enrager";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_AXE;
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
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Damage, 270, Stat.Strength, 150);
    }

    private final List<Requirement> requirements = List.of(new SkillRequirement(Skill.Combat, 25));

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    private static final GemstoneSlotType[] gemstoneSlots = {GemstoneSlotType.Sapphire};

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return gemstoneSlots;
    }

    private static final boolean[] unlocked = new boolean[]{true};

    @Override
    public boolean[] getUnlocked() {
        return unlocked;
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[0][];
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }

    @Override
    public int sellPrice() {
        return 100_000;
    }

    @Override
    public int getMaxStars() {
        return 10;
    }

    private static final Cost[][] costs = {{new EssenceCost(Essence.Crimson, 100), new ItemCost(BlazeAshes.class, 10)},
            {new EssenceCost(Essence.Crimson, 250), new ItemCost(BlazeAshes.class, 20)},
            {new EssenceCost(Essence.Crimson, 500), new ItemCost(BlazeAshes.class, 40)},
            {new EssenceCost(Essence.Crimson, 1_000), new ItemCost(BlazeAshes.class, 80), new CoinsCost(10_000)},
            {new EssenceCost(Essence.Crimson, 1_500), new ItemCost(BlazeAshes.class, 160), new CoinsCost(25_000)},
            {new EssenceCost(Essence.Crimson, 2_000), new ItemCost(BlazeAshes.class, 320), new CoinsCost(50_000)},
            {new EssenceCost(Essence.Crimson, 3_000), new ItemCost(BlazeAshes.class, 640), new CoinsCost(100_000)},
            {new EssenceCost(Essence.Crimson, 4_000), new ItemCost(MutatedBLazeAshes.class, 160), new ItemCost(HeavyPearl.class, 3), new CoinsCost(250_000)},
            {new EssenceCost(Essence.Crimson, 5_000), new ItemCost(MutatedBLazeAshes.class, 320), new ItemCost(HeavyPearl.class, 4), new CoinsCost(500_000)},
            {new EssenceCost(Essence.Crimson, 6_000), new ItemCost(MutatedBLazeAshes.class, 640), new ItemCost(HeavyPearl.class, 5), new CoinsCost(1_000_000)}};

    @Override
    public Cost[] upgradeCost(SbItemStack item, int star) {
        return costs[star];
    }
}
