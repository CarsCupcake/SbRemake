package me.carscupcake.sbremake.player.hotm.impl;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.PickaxeAbility;

public class ManiacMiner extends PickaxeAbility {
    public ManiacMiner(SkyblockPlayer player) {
        super(player, GreatExplorer.class);
    }

    @Override
    public int cooldown() {
        return 59;
    }

    @Override
    public void onInteract() {

    }

    @Override
    public String getName() {
        return "Maniac Miner";
    }

    @Override
    public String getId() {
        return "MANIAC_MINER_ABILITY";
    }

    @Override
    public Lore lore(int level) {
        return new Lore(STR."§7Spends all of your Mana and grants §a+1 \{Stat.MiningSpeed} §7for every 10 Mana spent, for §a15s.");
    }

    @Override
    public int levelRequirement() {
        return 6;
    }
}
