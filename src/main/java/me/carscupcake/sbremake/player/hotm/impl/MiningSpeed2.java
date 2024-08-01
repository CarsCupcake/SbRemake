package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class MiningSpeed2 extends HotmUpgrade {
    public MiningSpeed2(SkyblockPlayer player) {
        super(player, LonesomeMiner.class);
    }

    @Override
    public String getName() {
        return "Mining Speed II";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3.2d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GemstonePowder;
    }

    @Override
    public String getId() {
        return "MINING_SPEED_2";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Grants §a+%a% \{Stat.MiningSpeed}", Map.of("%a%", (_, _) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return level * 40;
    }

    @Override
    public int levelRequirement() {
        return 7;
    }
}
