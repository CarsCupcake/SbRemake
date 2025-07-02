package me.carscupcake.sbremake.item.impl.armor.minersOutfit;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class HasteFullSetBonus extends FullSetBonus {
    public static final FullSetBonus instance = new HasteFullSetBonus();

    public HasteFullSetBonus() {
        super("Haste", 4, 4, false);
    }

    @Override
    public void start(SkyblockPlayer player) {

    }

    @Override
    public void stop(SkyblockPlayer player) {

    }

    @Override
    public Lore lore() {
        return new Lore("§7Grants the wearer with §apermanent Haste II §7while worn.");
    }
}
