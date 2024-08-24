package me.carscupcake.sbremake.item.impl.armor.lapisArmor;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.FullSetBonus;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.util.HashSet;
import java.util.Set;

public class HealthFullSetBonus extends FullSetBonus {
    public static final HealthFullSetBonus INSTANCE = new HealthFullSetBonus();
    public static final Set<SkyblockPlayer> players = new HashSet<>();

    public HealthFullSetBonus() {
        super("Health", 4, 4, false);
    }


    @Override
    public void start(SkyblockPlayer player) {
        players.add(player);
    }

    @Override
    public void stop(SkyblockPlayer player) {
        players.remove(player);
    }

    @Override
    public Lore lore() {
        return new Lore(STR."§7Increase the wearesr's maximum \{Stat.Health.toString()}§7 by §a60");
    }
}
