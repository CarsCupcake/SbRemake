package me.carscupcake.sbremake.item.impl.armor.crimsonIsle;

import lombok.Getter;
import me.carscupcake.sbremake.item.impl.other.crimsonIsle.HeavyPearl;
import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.EssenceCost;
import me.carscupcake.sbremake.util.ItemCost;

@Getter
public enum KuudraArmorTier {
    Base("", "", new Cost[]{new EssenceCost(Essence.Crimson, 30)}, new Cost[]{new EssenceCost(Essence.Crimson, 35)},
            new Cost[]{new EssenceCost(Essence.Crimson, 40)}, new Cost[]{new EssenceCost(Essence.Crimson, 45), new CoinsCost(10_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 50), new CoinsCost(25_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 55),
            new CoinsCost(50_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 60), new CoinsCost(100_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 70), new ItemCost(HeavyPearl.class, 2), new CoinsCost(250_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 80), new ItemCost(HeavyPearl.class, 3), new CoinsCost(500_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 90), new ItemCost(HeavyPearl.class, 4), new CoinsCost(1_000_000)}),
    Hot("Hot", "HOT_", new Cost[]{new EssenceCost(Essence.Crimson, 170)}, new Cost[]{new EssenceCost(Essence.Crimson, 190)},
            new Cost[]{new EssenceCost(Essence.Crimson, 215)}, new Cost[]{new EssenceCost(Essence.Crimson, 240), new CoinsCost(10_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 270), new CoinsCost(25_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 300),
            new CoinsCost(50_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 340), new CoinsCost(100_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 390), new ItemCost(HeavyPearl.class, 3), new CoinsCost(250_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 440), new ItemCost(HeavyPearl.class, 4), new CoinsCost(500_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 500), new ItemCost(HeavyPearl.class, 5), new CoinsCost(1_000_000)}),
    Burning("Burning", "BURNING_", new Cost[]{new EssenceCost(Essence.Crimson, 900)}, new Cost[]{new EssenceCost(Essence.Crimson, 1_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 1_125)}, new Cost[]{new EssenceCost(Essence.Crimson, 1_270), new CoinsCost(10_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 1_450), new CoinsCost(25_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 1_650),
            new CoinsCost(50_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 1_850), new CoinsCost(100_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 2_100), new ItemCost(HeavyPearl.class, 3), new CoinsCost(250_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 2_350), new ItemCost(HeavyPearl.class, 4), new CoinsCost(500_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 2_650), new ItemCost(HeavyPearl.class, 5), new CoinsCost(1_000_000)}),
    Fiery("Fiery", "FIERY_", new Cost[]{new EssenceCost(Essence.Crimson, 5_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 5_600)},
            new Cost[]{new EssenceCost(Essence.Crimson, 6_300)}, new Cost[]{new EssenceCost(Essence.Crimson, 7_000), new CoinsCost(10_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 8_000), new CoinsCost(25_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 9_000),
            new CoinsCost(50_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 10_200), new CoinsCost(100_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 11_500), new ItemCost(HeavyPearl.class, 3), new CoinsCost(250_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 13_000), new ItemCost(HeavyPearl.class, 4), new CoinsCost(500_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 14_500), new ItemCost(HeavyPearl.class, 5), new CoinsCost(1_000_000)}),
    Infernal("Infernal", "INFERNAL_", new Cost[]{new EssenceCost(Essence.Crimson, 30_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 35_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 41_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 48_000), new CoinsCost(10_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 56_000), new CoinsCost(25_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 65_500),
            new CoinsCost(50_000)}, new Cost[]{new EssenceCost(Essence.Crimson, 76_000), new CoinsCost(100_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 89_000), new ItemCost(HeavyPearl.class, 3), new CoinsCost(250_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 105_000), new ItemCost(HeavyPearl.class, 4), new CoinsCost(500_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 120_000), new ItemCost(HeavyPearl.class, 5), new CoinsCost(1_000_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 140_000), new ItemCost(HeavyPearl.class, 6), new CoinsCost(2_500_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 165_000), new ItemCost(HeavyPearl.class, 7), new CoinsCost(5_000_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 192_000), new ItemCost(HeavyPearl.class, 8), new CoinsCost(10_000_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 225_000), new ItemCost(HeavyPearl.class, 9), new CoinsCost(25_000_000)},
            new Cost[]{new EssenceCost(Essence.Crimson, 265_000), new ItemCost(HeavyPearl.class, 10), new CoinsCost(50_000_000)});
    private final String name;
    private final String id;
    private final Cost[][] costs;

    KuudraArmorTier(String name, String id, Cost[]... cost) {
        this.id = id;
        this.name = name;
        costs = cost;
    }
}
