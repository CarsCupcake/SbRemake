package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class HomingMissiles extends FullSetBonus {
    public static final HomingMissiles INSTANCE = new HomingMissiles();
    public HomingMissiles() {
        super("Homing Missiles", 4, 2, false);
    }

    @Override
    public void start(SkyblockPlayer player) {

    }

    @Override
    public void stop(SkyblockPlayer player) {

    }

    @Override
    public Lore lore() {
        return Lore.EMPTY;
    }
}
