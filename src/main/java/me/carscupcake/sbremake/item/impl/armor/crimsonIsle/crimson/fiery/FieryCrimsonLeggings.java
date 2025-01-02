package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonLeggingsBaseline;

import java.util.Map;

public class FieryCrimsonLeggings extends CrimsonLeggingsBaseline {
    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Fiery;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 410, Stat.Defense, 110, Stat.Strength, 60, Stat.Intelligence, 10, Stat.CritDamage, 40);
    }
}
