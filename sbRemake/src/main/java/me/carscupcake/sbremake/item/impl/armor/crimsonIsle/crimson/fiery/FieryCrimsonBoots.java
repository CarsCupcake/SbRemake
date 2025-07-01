package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonBootsBaseline;

import java.util.Map;

public class FieryCrimsonBoots extends CrimsonBootsBaseline {
    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Fiery;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 260, Stat.Defense, 80, Stat.Strength, 60, Stat.Intelligence, 10, Stat.CritDamage, 40);
    }
}
