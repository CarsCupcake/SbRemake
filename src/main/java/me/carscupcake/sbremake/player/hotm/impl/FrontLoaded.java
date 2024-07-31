package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class FrontLoaded extends HotmUpgrade {
    public FrontLoaded(SkyblockPlayer player) {
        super(player, Crystallized.class);
    }

    @Override
    public String getName() {
        return "Front Loaded";
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
        return "FRONT_LOADED";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Grants §a+100 \{Stat.MiningSpeed} §7and \{Stat.MiningFortune} §7as well as §7+2 Base Powder §7for the first §e2,500 §7ores you mine in a day.");
    }

    @Override
    public int levelRequirement() {
        return 4;
    }
}
