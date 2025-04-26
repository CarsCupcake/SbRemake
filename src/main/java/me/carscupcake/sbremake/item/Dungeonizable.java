package me.carscupcake.sbremake.item;

import me.carscupcake.sbremake.player.Essence;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Cost;

public interface Dungeonizable extends StarUpgradable {
    @Override
    default double getBonus(SkyblockPlayer player, int stars) {
        //TODO Dungeon
        return StarUpgradable.super.getBonus(player, stars);
    }

    int getCost(int star);

    Essence getEssence();

    @Override
    default Cost[] upgradeCost(SbItemStack item, int star) {
        return new Cost[0];
    }
}

