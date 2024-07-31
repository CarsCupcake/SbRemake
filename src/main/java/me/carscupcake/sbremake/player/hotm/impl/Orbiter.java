package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class Orbiter extends HotmUpgrade {
    public Orbiter(SkyblockPlayer player) {
        super(player, MiningFortune.class);
    }

    @Override
    public String getName() {
        return "Orbiter";
    }

    @Override
    public int getMaxLevel() {
        return 80;
    }

    @Override
    public int nextLevelCost(int current) {
        return (current + 1) + 70;
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "ORBITER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7When mining ores, you have a §a%p%% §7chance to get a random amount of experiences orbs..", Map.of("%p%", (_, _) -> StringUtils.cleanDouble(bonus(level))));
    }

    public double bonus(int level) {
        return 0.2 + (level  * 0.01);
    }

    @Override
    public int levelRequirement() {
        return 3;
    }
}
