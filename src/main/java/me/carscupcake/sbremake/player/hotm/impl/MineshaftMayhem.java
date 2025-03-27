package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.List;

public class MineshaftMayhem extends HotmUpgrade {

    public MineshaftMayhem(SkyblockPlayer player) {
        super(player);
    }

    @Override
    public String getName() {
        return "Mineshaft Mayhem";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int nextLevelCost(int current) {
        return 0;
    }

    @Override
    public Powder upgradeType(int current) {
        return Powder.GlacialPowder;
    }

    @Override
    public String getId() {
        return "MINESHAFT_MAYHEM";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(List.of("§7Every time you enter a §bGlacite", "§bMineshaft§7, you receive a random buff.", " ",
                "§7Possible Buffs",
                "§8 ■ §a+5% §7chance to find a §9Suspicious Scrap§7.",
                "§8 ■ §7Gain §a+100 " + (Stat.MiningFortune) ,
                "§8 ■ §7Gain §a+200 " + (Stat.MiningSpeed) ,
                "§8 ■ §7Gain +10 " + (Stat.ColdResistance) ,
                "§8 ■ §7Reduce Pickaxe Ability cooldown by §a25%"));
    }

    @Override
    public int levelRequirement() {
        return 8;
    }
}
