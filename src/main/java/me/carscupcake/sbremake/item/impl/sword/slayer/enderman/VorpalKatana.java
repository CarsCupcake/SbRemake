package me.carscupcake.sbremake.item.impl.sword.slayer.enderman;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.ability.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneItem;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class VorpalKatana implements ISbItem, ISbItem.StatProvider, GemstoneSlots {
    private final Map<Stat, Number> stats = Map.of(Stat.Damage, 190, Stat.Strength, 80, Stat.CritDamage, 30, Stat.Intelligence, 200);
    private final List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Enderman, 5));
    private final Lore lore =
            new Lore("§7Deals §a+250% §7damage to Endermen. Receive §a9%§7 less damage from Endermen when held.\n§7Gain " + Stat.CombatWisdom.getPrefix() + "+30 " + Stat.CombatWisdom + "§7 agains Enderman");
    private final List<Ability> ability = List.of(new ItemAbility<>("Soulcry", AbilityType.RIGHT_CLICK, new SoulcryAbility(300), new Lore("§7Gain " +
                                                                                                                                                  "§c+300 ⫽Ferocity§7 against Endermen for §a4s§7."), new SoulcryAbility(0), new ManaRequirement<>(200), new CooldownRequirement<>(4), new SoulflowRequirement<>(2)));
    private final GemstoneSlotType[] slots = {GemstoneSlotType.Jasper, GemstoneSlotType.Sapphire};
    private final boolean[] unlocked = {false, false};
    private final Cost[][] costs = {{new CoinsCost(50_000), new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Fine)
                                                                                 .asItem(), 20)}, {new CoinsCost(100_000),
            new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Sapphire)
                                                                                                                                                .get(Gemstone.Quality.Fine)
                                                                                                                                                .asItem(), 40)}};

    @Override
    public String getId() {
        return "VORPAL_KATANA";
    }

    @Override
    public String getName() {
        return "Vorpal Katana";
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
        return ItemRarity.EPIC;
    }

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    @Override
    public Lore getLore() {
        return lore;
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return ability;
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return slots;
    }

    @Override
    public boolean[] getUnlocked() {
        return unlocked;
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return costs;
    }
}
