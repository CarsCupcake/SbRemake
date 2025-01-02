package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.burning;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonBootsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonLeggingsBaseline;

import java.util.Map;

public class BurningCrimsonBoots extends CrimsonBootsBaseline {
    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Burning;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 206, Stat.Defense, 64, Stat.Strength, 48, Stat.Intelligence, 8, Stat.CritDamage, 32);
    }
}
