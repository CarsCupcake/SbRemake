package me.carscupcake.sbremake.item.impl.sword.dungeons;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;

import java.util.Map;

public class Scylla extends NecronBlade implements GemstoneSlots {
    private static final GemstoneSlotType[] gemstoneSlots = {GemstoneSlotType.Combat, GemstoneSlotType.Combat};
    private static final boolean[] unlocked = {false, false};
    private static final Cost[][] costs = {{new CoinsCost(250_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Sapphire).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Ruby).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Amethyst).get(Gemstone.Quality.Flawless).asItem(), 1)}, {new CoinsCost(250_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Sapphire).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Ruby).get(Gemstone.Quality.Flawless).asItem(), 1), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Amethyst).get(Gemstone.Quality.Flawless).asItem(), 1)}};
    private final Map<Stat, Number> stats = Map.of(Stat.Damage, 270, Stat.Strength, 150, Stat.CritChance, 12, Stat.CritDamage, 35, Stat.Intelligence, 50, Stat.Ferocity, 30);

    @Override
    public String getId() {
        return "SCYLLA";
    }

    @Override
    public String getName() {
        return "Scylla";
    }

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §c+50% §7damage to Withers. Grants §c+1 " + Stat.Damage + " §7and §9+1 " + Stat.CritDamage + " §7per §cCatacombs §7level.");
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return gemstoneSlots;
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
