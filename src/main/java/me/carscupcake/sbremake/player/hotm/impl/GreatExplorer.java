package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class GreatExplorer extends HotmUpgrade {

    public GreatExplorer(SkyblockPlayer player) {
        super(player, MiningFortune2.class, Fortunate.class, StarPowder.class);
    }

    @Override
    public String getName() {
        return "Great Explorer";
    }

    @Override
    public int getMaxLevel() {
        return 20;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(current + 2, 4d);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GemstonePowder;
    }

    @Override
    public String getId() {
        return "GREAT_EXPLORER";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Grants §a+%p% §7chance to find treasure", Map.of("%p%", (ignored, ignored2) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return 20 + (4 * (level - 1));
    }

    @Override
    public int levelRequirement() {
        return 6;
    }
}
