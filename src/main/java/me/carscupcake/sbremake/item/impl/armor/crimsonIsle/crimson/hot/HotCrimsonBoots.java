package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.hot;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonBootsBaseline;

import java.util.Map;

public class HotCrimsonBoots extends CrimsonBootsBaseline {
    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 164, Stat.Defense, 50, Stat.Strength, 38, Stat.Intelligence, 6, Stat.CritDamage, 25);
    }
}
