package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class SubzeroMining extends HotmUpgrade {

    public SubzeroMining(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "ZubZero Mining";
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 2.3);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "ZUBZERO_MINING";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Grants §a+%b% \{Stat.MiningFortune} §7when mining §bGlacite§7.", Map.of("%b%", (_, _) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return level;
    }

    @Override
    public int levelRequirement() {
        return 9;
    }
}
