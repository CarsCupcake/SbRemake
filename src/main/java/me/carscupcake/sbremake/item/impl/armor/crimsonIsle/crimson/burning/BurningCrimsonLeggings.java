package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.burning;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonLeggingsBaseline;

import java.util.Map;

public class BurningCrimsonLeggings extends CrimsonLeggingsBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Burning;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 325, Stat.Defense, 87, Stat.Strength, 48, Stat.Intelligence, 8, Stat.CritDamage, 32);
    }
}
