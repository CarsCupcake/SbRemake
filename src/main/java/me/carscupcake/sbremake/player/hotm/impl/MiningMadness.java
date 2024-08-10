package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class MiningMadness extends HotmUpgrade {
    public MiningMadness(SkyblockPlayer player) {
        super(player, LuckOfTheCave.class, GoblinKiller.class, SeasonedMineman.class);
    }

    @Override
    public String getName() {
        return "Mining Madness";
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
        return "MINING_MADNESS";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Grants §a+%a% \{Stat.MiningSpeed}§7 and \{Stat.MiningFortune}", Map.of("%a%", (_, _) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return 50;
    }

    @Override
    public int levelRequirement() {
        return 4;
    }
}
