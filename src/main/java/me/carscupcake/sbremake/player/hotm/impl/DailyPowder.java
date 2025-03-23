package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class DailyPowder extends HotmUpgrade {
    public DailyPowder(SkyblockPlayer player) {
        super(player, MiningFortune.class, EfficientMiner.class);
    }

    @Override
    public String getName() {
        return "Daily Powder";
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public int nextLevelCost(int current) {
        return (current * 18) + 200;
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "DAILY_POWDER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain §a%p% Powder §7from the first ore you mine every day. Works for all powder types.", Map.of("%p%", (_, _) -> String.valueOf(bonus(level))));
    }

    public int bonus(int level) {
        return (200 + ((level - 1) * 18)) * 2;
    }

    @Override
    public int levelRequirement() {
        return 3;
    }
}
