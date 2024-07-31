package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.Map;

public class QuickForge extends HotmUpgrade {
    public QuickForge(SkyblockPlayer player) {
        super(player, MiningFortune.class);
    }

    @Override
    public String getName() {
        return "Quick Forge";
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
        return Powder.MithrilPowder;
    }

    @Override
    public String getId() {
        return "TITANIUM_INSANIUM";
    }

    @Override
    public Lore lore(int level) {
        return new Lore("§7Decreases the time it takes to forge by §a%ch%%§7.", Map.of("%ch%", (item, player) -> String.valueOf(reward(level))));
    }

    public double reward(int level) {
        return Math.min(30d, 10 + (level * 0.5) + Math.floor(level / 20d) * 20);
    }

    @Override
    public int levelRequirement() {
        return 2;
    }
}
