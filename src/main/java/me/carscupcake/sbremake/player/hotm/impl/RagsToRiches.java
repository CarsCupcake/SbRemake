package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.util.StringUtils;
import org.antlr.runtime.misc.Stats;

import java.util.Map;

public class RagsToRiches extends HotmUpgrade {

    public RagsToRiches(SkyblockPlayer player) {
        super(player, Excavator.class, EagerAdventurer.class);
    }

    @Override
    public String getName() {
        return "Rags to Riches";
    }

    @Override
    public int getMaxLevel() {
        return 50;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) Math.pow(level + 1, 3);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "RAGS_TO_RICHES";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7GRants §a+%b% \{Stat.MiningFortune} §7while inside a §bGlacite Mineshaft", Map.of("%b%", (_, _) -> StringUtils.cleanDouble(getBonus(level), 1)));
    }

    public double getBonus(int level) {
        return level * 2;
    }

    @Override
    public int levelRequirement() {
        return 10;
    }
}
