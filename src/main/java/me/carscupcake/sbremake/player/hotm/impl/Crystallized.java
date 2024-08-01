package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class Crystallized extends HotmUpgrade {
    public Crystallized(SkyblockPlayer player) {
        super(player, Pickobulus.class, FrontLoaded.class);
    }

    @Override
    public String getName() {
        return "Crystallized";
    }

    @Override
    public int getMaxLevel() {
        return 30;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 3.4);
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
        return new Lore(STR."§7Grants §a+%speed% \{Stat.MiningSpeed} §7and §a+%fortune% \{Stat.MiningFortune} §7near §5Fallen Stars§7.", Map.of("%speed%", (_, _) -> String.valueOf(miningSpeed(level)),
                "%fortune%", (_, _) -> String.valueOf(miningFortune(level))));
    }

    public int miningSpeed(int level) {
        return 20 + ((level - 1) * 6);
    }
    public int miningFortune(int level) {
        return 20 + ((level - 1) * 5);
    }

    @Override
    public int levelRequirement() {
        return 3;
    }
}
