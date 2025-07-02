package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.burning;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraLeggingsBaseline;

import java.util.Map;

public class BurningAuroraLeggings extends AuroraLeggingsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 325, Stat.Defense, 87, Stat.Intelligence, 198);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Burning;
    }
}
