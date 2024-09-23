package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonBootsBaseline;

import java.util.Map;

public class InfernalCrimsonBoots extends CrimsonBootsBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 328, Stat.Defense, 101, Stat.Strength, 76, Stat.Intelligence, 13, Stat.CritDamage, 50);
    }
}
