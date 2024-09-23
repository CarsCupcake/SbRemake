package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonChestplateBaseline;

import java.util.Map;

public class FieryCrimsonChestplate extends CrimsonChestplateBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Fiery;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 460, Stat.Defense, 130, Stat.Strength, 60, Stat.Intelligence, 10, Stat.CritDamage, 40);
    }
}
