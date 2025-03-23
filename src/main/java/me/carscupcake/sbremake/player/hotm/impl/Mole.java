package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class Mole extends HotmUpgrade {

    public Mole(SkyblockPlayer player) {
        super(player, PeakOfTheMountain.class, Fortunate.class, Professional.class, PowderBuff.class);
    }

    @Override
    public String getName() {
        return "Mole";
    }

    @Override
    public int getMaxLevel() {
        return 190;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 2.2d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GemstonePowder;
    }

    @Override
    public String getId() {
        return "MOLE";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7When mining hard stone, you have a §a%c%% §7chance to mine §a%b% §7adjacent hart stone blocks.", Map.of("%b%", (_, _) -> String.valueOf(getBlocks(level)),
                "%c%", (_, _) -> String.valueOf(getChance(level))));
    }

    public int getBlocks(int level) {
        return (int) (((50 + ((level-1) * 5)) / 100d) + 1);
    }

    public int getChance(int level) {
        return (50 + ((level - 1) * 5)) - (getBlocks(level) - 1) * 100;
    }

    @Override
    public int levelRequirement() {
        return 6;
    }
}
