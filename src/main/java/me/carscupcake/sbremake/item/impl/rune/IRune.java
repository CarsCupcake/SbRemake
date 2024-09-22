package me.carscupcake.sbremake.item.impl.rune;

import me.carscupcake.sbremake.item.ItemRarity;
import net.minestom.server.entity.Entity;

public sealed interface IRune<T extends Entity> permits WeaponRune, ArmorRune, BowRune {
    RuneTicker<T> createTicker(T entity, int level);
    int maxRuneLevel();
    ItemRarity getRarity();
    String getId();
    String getName();
    String headValue();
}
