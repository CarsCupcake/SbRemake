package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.hot;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonLeggingsBaseline;

import java.util.Map;

public class HotCrimsonLeggings extends CrimsonLeggingsBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 258, Stat.Defense, 69, Stat.Strength, 38, Stat.Intelligence, 6, Stat.CritDamage, 25);
    }
}
