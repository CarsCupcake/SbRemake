package me.carscupcake.sbremake.item.impl.armor.dragon.superior;

import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class SuperiorBloodFullSetBonus extends FullSetBonus {
    public static final SuperiorBloodFullSetBonus INSTANCE = new SuperiorBloodFullSetBonus();
    public SuperiorBloodFullSetBonus() {
        super("Superior Blood", 4, 4, false);
    }

    @Override
    public void start(SkyblockPlayer player) {}

    @Override
    public void stop(SkyblockPlayer player) {

    }

    @Override
    public Lore lore() {
        return new Lore("§7Most of your stats are increased by§a 5%§7 and§6 Aspect of the Dragons§7 ability deals§a 50%§7 more damage.");
    }
}
