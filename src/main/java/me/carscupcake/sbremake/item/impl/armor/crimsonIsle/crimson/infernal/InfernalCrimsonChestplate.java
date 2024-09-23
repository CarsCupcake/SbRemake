package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonChestplateBaseline;

import java.util.Map;

public class InfernalCrimsonChestplate extends CrimsonChestplateBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 580, Stat.Defense, 164, Stat.Strength, 76, Stat.Intelligence, 13, Stat.CritDamage, 50);
    }
}
