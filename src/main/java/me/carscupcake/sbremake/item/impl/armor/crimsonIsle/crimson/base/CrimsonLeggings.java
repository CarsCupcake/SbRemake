package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonLeggingsBaseline;

import java.util.Map;

public class CrimsonLeggings extends CrimsonLeggingsBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 205, Stat.Defense, 55, Stat.Strength, 30, Stat.Intelligence, 5, Stat.CritDamage, 20);
    }
}
