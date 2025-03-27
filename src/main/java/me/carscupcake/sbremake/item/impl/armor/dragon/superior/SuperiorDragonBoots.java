package me.carscupcake.sbremake.item.impl.armor.dragon.superior;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.modifiers.reforges.ArmorReforge;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class SuperiorDragonBoots implements ISbItem, ISbItem.StatProvider, NpcSellable, ColoredLeather, Listener, GemstoneSlots {
    @Override
    public String getId() {
        return "SUPERIOR_DRAGON_BOOTS";
    }

    @Override
    public String getName() {
        return "Superior Dragon Boots";
    }

    @Override
    public Material getMaterial() {
        return Material.LEATHER_BOOTS;
    }

    @Override
    public ItemType getType() {
        return ItemType.Boots;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 80, Stat.Defense, 110, Stat.Speed, 3, Stat.Strength, 10, Stat.Intelligence, 25, Stat.CritChance, 2, Stat.CritDamage, 10);
    }

    @Override
    public int sellPrice() {
        return 100_000;
    }

    @Override
    public Color color() {
        return new Color(0xf25d18);
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SkillRequirement(Skill.Combat, 20));
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(SuperiorBloodFullSetBonus.INSTANCE);
    }

    private static final Set<Stat> allowed = Set.of(Stat.Health, Stat.Defense, Stat.Strength, Stat.Intelligence, Stat.CritChance, Stat.CritDamage, Stat.AttackSpeed, Stat.AbilityDamage, Stat.TrueDefense, Stat.Ferocity, Stat.Speed, Stat.MagicFind, Stat.PetLuck, Stat.SeaCreatureChance, Stat.ColdResistance);

    @Override
    public EventNode<Event> node() {
        return EventNode.all("set.superior").addListener(PlayerStatEvent.class, event -> {
            if (!allowed.contains(event.stat())) return;
            if (event.player().getFullSetBonusPieceAmount(SuperiorBloodFullSetBonus.INSTANCE) >= 4)
                event.modifiers().add(new PlayerStatEvent.BasicModifier("Superior Blood", 0.05, PlayerStatEvent.Type.AddativeMultiplier, PlayerStatEvent.StatsCategory.Ability));
            for (EquipmentSlot equipmentSlot : EquipmentSlot.armors()) {
                SbItemStack itemStack = event.player().getSbEquipment(equipmentSlot);
                if (itemStack != null && itemStack.getModifier(Modifier.REFORGE) == ArmorReforge.Renowned)
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Renowned " + (itemStack.sbItem().getType()) , 0.01, PlayerStatEvent.Type.AddativeMultiplier, PlayerStatEvent.StatsCategory.Armor));

            }
        });
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
