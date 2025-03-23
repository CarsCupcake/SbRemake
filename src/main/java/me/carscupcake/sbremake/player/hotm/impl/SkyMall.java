package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;

import java.util.List;

public class SkyMall extends HotmUpgrade {
    public SkyMall(SkyblockPlayer player) {
        super(player, MiningMadness.class);
    }

    @Override
    public String getName() {
        return "Sky Mall";
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
        return "SKY_MALL";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(List.of("§7Every SkyBlock day, you receive", "§7a random buff in the §2Dwarven", "§2Mines§7.", " ", "§7Possible Buffs"
                ,"§8 ■ §7Gain §a+100 " + (Stat.MiningSpeed) 
                ,"§8 ■ §7Gain §a+100 " + (Stat.MiningSpeed) 
                ,"§8 ■ §7Gain §a+15& §7 chance to gain", "   §7extra Powder while mining."
                ,"§8 ■ §7Reduce Pickaxe Ability cooldown", "   §7by §a20%"
                ,"§8 ■ §a10x §7chance to find Goblins", "   §7while mining."
                ,"§8 ■ §7Gain §a5x §9Titanium §7drops."
        ));
    }

    @Override
    public int levelRequirement() {
        return 4;
    }
}
