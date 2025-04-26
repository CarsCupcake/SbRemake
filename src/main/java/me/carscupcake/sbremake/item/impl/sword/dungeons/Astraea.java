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

public class Astraea extends NecronBlade implements GemstoneSlots {
    @Override
    public String getId() {
        return "ASTRAEA";
    }

    @Override
    public String getName() {
        return "Astraea";
    }

    private final Map<Stat, Number> stats = Map.of(Stat.Damage, 270, Stat.Strength, 150, Stat.Defense, 250, Stat.Intelligence, 50, Stat.TrueDefense, 20, Stat.Ferocity, 30);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §c+50% §7damage to Withers. Grants §c+1 " + Stat.Damage + " §7and §a+2 " + Stat.Defense + " §7per §cCatacombs §7level.");
    }

    private static final GemstoneSlotType[] gemstoneSlots = {GemstoneSlotType.Defensive, GemstoneSlotType.Combat};

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return gemstoneSlots;
    }

    private static final boolean[] unlocked = {false, false};
    @Override
    public boolean[] getUnlocked() {
        return unlocked;
    }

    private static final Cost[][] costs = {{new CoinsCost(250_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Amethyst).get(Gemstone.Quality.Flawless).asItem(), 2),
            new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Ruby).get(Gemstone.Quality.Flawless).asItem(), 2)},
            {new CoinsCost(250_000),
                    new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Flawless).asItem(), 1),
                    new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Sapphire).get(Gemstone.Quality.Flawless).asItem(), 1),
                    new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Ruby).get(Gemstone.Quality.Flawless).asItem(), 1),
                    new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Amethyst).get(Gemstone.Quality.Flawless).asItem(), 1)}};

    @Override
    public Cost[][] getLockedSlotCost() {
        return costs;
    }
}
