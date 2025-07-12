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
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class VoidedgeKatana implements ISbItem, ISbItem.StatProvider, Listener, GemstoneSlots {
    private final Map<Stat, Number> stats = Map.of(Stat.Damage, 155, Stat.Strength, 60, Stat.CritDamage, 25, Stat.Intelligence, 50);
    private final List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Enderman, 3));
    private final Lore lore =
            new Lore("§7Deals §a+200% §7damage to Endermen. Receive §a6%§7 less damage from Endermen when held.\n§7Gain " + Stat.CombatWisdom.getPrefix() + "+15 " + Stat.CombatWisdom + "§7 agains Enderman");
    private final List<Ability> ability = List.of(new ItemAbility<>("Soulcry", AbilityType.RIGHT_CLICK, new SoulcryAbility(200), new Lore("§7Gain §c+200 ⫽Ferocity§7 against Endermen for §a4s§7."), new SoulcryAbility(0), new ManaRequirement<>(200), new CooldownRequirement<>(4), new SoulflowRequirement<>(2)));
    private final GemstoneSlotType[] slots = {GemstoneSlotType.Jasper, GemstoneSlotType.Sapphire};
    private final boolean[] unlocked = {false, false};
    private final Cost[][] costs = {{new CoinsCost(50_000), new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Fine)
                                                                                 .asItem(), 20)}, {new CoinsCost(100_000),
            new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Sapphire)
                                                                                                                                                .get(Gemstone.Quality.Fine)
                                                                                                                                                .asItem(), 40)}};

    @Override
    public String getId() {
        return "VOIDEDGE_KATANA";
    }

    @Override
    public String getName() {
        return "Voidedge Katana";
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
    public EventNode<Event> node() {
        return SoulcryAbility.LISTENER;
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
