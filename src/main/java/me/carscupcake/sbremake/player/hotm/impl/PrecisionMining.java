package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

public class PrecisionMining extends HotmUpgrade {
    public PrecisionMining(SkyblockPlayer player) {
        super(player, FrontLoaded.class);
    }

    @Override
    public String getName() {
        return "Precision Mining";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "PRECISION_MINING";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7When mining ores, a particle target appears on the block that increases your " + (Stat.MiningSpeed) + " §7by §a30% §7when aiming at it.");
    }

    @Override
    public int levelRequirement() {
        return 4;
    }
}
