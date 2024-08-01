package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;

public class MiningSpeedBoost extends PickaxeAbility {
    public MiningSpeedBoost(SkyblockPlayer player) {
        super(player, TitaniumInsanium.class, LuckOfTheCave.class);
    }

    @Override
    public int cooldown() {
        return 120;
    }

    @Override
    public void onInteract() {

    }

    @Override
    public String getName() {
        return "Mining Speed Boost";
    }

    @Override
    public String getId() {
        return "MINING_SPEED_BOOST_ABILITY";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Grants +300 \{Stat.MiningSpeed} §7for §a20s§7.");
    }

    @Override
    public int levelRequirement() {
        return 2;
    }
}
