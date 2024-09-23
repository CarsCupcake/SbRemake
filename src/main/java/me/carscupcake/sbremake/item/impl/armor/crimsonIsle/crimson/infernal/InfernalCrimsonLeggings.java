package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonLeggingsBaseline;

import java.util.Map;

public class InfernalCrimsonLeggings extends CrimsonLeggingsBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 517, Stat.Defense, 139, Stat.Strength, 76, Stat.Intelligence, 13, Stat.CritDamage, 50);
    }
}
