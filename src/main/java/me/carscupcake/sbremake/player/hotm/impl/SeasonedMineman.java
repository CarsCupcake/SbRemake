package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;

import java.util.Map;

public class SeasonedMineman extends HotmUpgrade {
    public SeasonedMineman(SkyblockPlayer player) {
        super(player, MiningFortune.class);
    }

    @Override
    public String getName() {
        return "Seasoned Mineman";
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 2.3d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "SEASONED_MINEMAN";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Grands §3+%a% \{Stat.MiningWisdom}", Map.of("%a%", (_, _) -> StringUtils.cleanDouble(bonus(level))));
    }

    public double bonus(int level) {
        return 5 + (level * 0.1d);
    }

    @Override
    public int levelRequirement() {
        return 3;
    }
}
