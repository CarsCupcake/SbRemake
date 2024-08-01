package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class EfficientMiner extends HotmUpgrade {
    public EfficientMiner(SkyblockPlayer player) {
        super(player, DailyPowder.class, SeasonedMineman.class, Orbiter.class);
    }

    @Override
    public String getName() {
        return "Efficient Miners";
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 2.6d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "EFFICIENT_MINER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7When mining ores, you have a §a%a%% §7chance to mine %b% adjacent ores.",
                Map.of("%a%", (_, _) -> String.valueOf(getPercentage(level)), "%b%", (_, _) -> String.valueOf(getBlocks(level))));
    }

    public double getPercentage(int level) {
        return 10 + (level * 0.4);
    }
    public int getBlocks(int level) {
        return (int) (1 + (level / 20d));
    }

    @Override
    public int levelRequirement() {
        return 4;
    }
}
