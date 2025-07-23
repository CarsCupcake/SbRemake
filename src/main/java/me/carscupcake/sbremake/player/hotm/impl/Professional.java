package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class Professional extends HotmUpgrade {

    public Professional(SkyblockPlayer player) {
        super(player, LonesomeMiner.class, Mole.class);
    }

    @Override
    public String getName() {
        return "Professional";
    }

    @Override
    public int getMaxLevel() {
        return 140;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 2.3d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GemstonePowder;
    }

    @Override
    public String getId() {
        return "PROFESSIONAL";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Gain §a+%b% " + (Stat.MiningSpeed) + " §7when mining Gemstones.", Map.of("%b%", (ignored, ignored2) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return 50 + (level * 5);
    }

    @Override
    public int levelRequirement() {
        return 6;
    }
}
