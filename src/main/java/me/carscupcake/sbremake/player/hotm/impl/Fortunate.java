package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class Fortunate extends HotmUpgrade {

    public Fortunate(SkyblockPlayer player) {
        super(player, Mole.class, GreatExplorer.class);
    }

    @Override
    public String getName() {
        return "Fortunate";
    }

    @Override
    public int getMaxLevel() {
        return 20;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3.05d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "FORTUNATE";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain §a+%b% " + (Stat.MiningFortune) + " §7when mining Gemstones.", Map.of("%b%", (ignored, ignored2) -> {
            return StringUtils.cleanDouble(getBonus(level), 1);
        }));
    }

    public double getBonus(int level) {
        return 20 + level * 4;
    }

    @Override
    public int levelRequirement() {
        return 6;
    }
}
