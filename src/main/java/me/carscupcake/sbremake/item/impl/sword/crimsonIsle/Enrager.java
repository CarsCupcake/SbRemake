package me.carscupcake.sbremake.item.impl.sword.crimsonIsle;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.ability.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.Cost;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Enrager implements ISbItem, ISbItem.StatProvider, GemstoneSlots {
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
}
