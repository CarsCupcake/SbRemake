package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.List;
import java.util.Map;

public class DailyGrind extends HotmUpgrade {

    public DailyGrind(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Daily Grind";
    }

    @Override
    public int getMaxLevel() {
        return 100;
    }

    @Override
    public int nextLevelCost(int current) {
        return (int) 200 + ((level  - 1) * 18);
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "DAILY_GRIND";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(List.of("§7Your first daily commission in each", "§7Mining Zone grants bonus powder:", " ",
                "§2Dwarven Mines§7: §a+%b% §2Mithril Powder", "§5Crystal Hollows§7: §a+%b% §bGemstone Powder", "§bGlacite Tunnels§7: §a+%b% §bGlacite Powder"), Map.of("%b%", (_, _) -> String.valueOf(getBonus(level))));
    }

    public int getBonus(int level) {
        return level * 50;
    }

    @Override
    public int levelRequirement() {
        return 8;
    }
}
