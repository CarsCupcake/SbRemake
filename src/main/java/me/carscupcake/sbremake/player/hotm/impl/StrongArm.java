package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class StrongArm extends HotmUpgrade {

    public StrongArm(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Strong Arm";
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
        return "STRONG_ARM";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain §a+%b% " + (Stat.MiningSpeed) + " §7when mining Tungsten or Umber.", Map.of("%b%", (ignored, ignored2) -> String.valueOf(getBonus(level))));
    }

    public double getBonus(int level) {
        return level * 5;
    }

    @Override
    public int levelRequirement() {
        return 8;
    }
}
