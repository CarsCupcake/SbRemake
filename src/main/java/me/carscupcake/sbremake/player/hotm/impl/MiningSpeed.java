package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class MiningSpeed extends HotmUpgrade {
    public MiningSpeed(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Mining Speed";
    }

    @Override
    public int getMaxLevel() {
        return 50;
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
        return "MINING_SPEED_1";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Grants §a+%a% " + (Stat.MiningSpeed), Map.of("%a%", (ignored, ignored2) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return level * 20;
    }

    @Override
    public int levelRequirement() {
        return 1;
    }
}
