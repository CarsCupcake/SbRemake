package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonChestplateBaseline;

import java.util.Map;

public class CrimsonChestplate extends CrimsonChestplateBaseline {
    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 230, Stat.Defense, 65, Stat.Strength, 30, Stat.Intelligence, 5, Stat.CritDamage, 20);
    }
}
