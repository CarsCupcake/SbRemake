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

public class Valkyrie extends NecronBlade implements GemstoneSlots {
    @Override
    public String getId() {
        return "VALKYRIE";
    }

    @Override
    public String getName() {
        return "Valkyrie";
    }

    private final Map<Stat, Number> stats = Map.of(Stat.Damage, 270, Stat.Strength, 145, Stat.Intelligence, 60, Stat.Ferocity, 60);

    @Override
    public Map<Stat, Number> stats() {
        return stats;
    }

    @Override
    public Lore getLore() {
        return new Lore("§7Deals §c+50% §7damage to Withers. Grants §c+1 " + Stat.Damage + " §7and §c+1 " + Stat.Strength + " §7per §cCatacombs §7level.");
    }

    private static final GemstoneSlotType[] gemstoneSlots = {GemstoneSlotType.Jasper, GemstoneSlotType.Combat};

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return gemstoneSlots;
    }

    private static final boolean[] unlocked = {false, false};
    @Override
    public boolean[] getUnlocked() {
        return unlocked;
    }

    private static final Cost[][] costs = {{new CoinsCost(250_000), new ItemCost(Gemstone.gemstones.get(Gemstone.Type.Jasper).get(Gemstone.Quality.Flawless).asItem(), 4)},
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
