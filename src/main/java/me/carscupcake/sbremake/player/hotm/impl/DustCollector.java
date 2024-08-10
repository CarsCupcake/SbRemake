package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class DustCollector extends HotmUpgrade {

    public DustCollector(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Dust Collector";
    }

    @Override
    public int getMaxLevel() {
        return 20;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 1, 4);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "DUST_COLLECTOR";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Receive §a%b%% §7more §fFossile Dust §7from all sources.", Map.of("%b%", (_, _) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return level;
    }

    @Override
    public int levelRequirement() {
        return 8;
    }
}
