package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraLeggingsBaseline;

import java.util.Map;

public class FieryAuroraLeggings extends AuroraLeggingsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 410, Stat.Defense, 110, Stat.Intelligence, 250);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Fiery;
    }
}
