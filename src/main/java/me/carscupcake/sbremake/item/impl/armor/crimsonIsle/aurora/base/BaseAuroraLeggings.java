package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraBootsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraLeggingsBaseline;

import java.util.Map;

public class BaseAuroraLeggings extends AuroraLeggingsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 205, Stat.Defense, 55, Stat.Intelligence, 125);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }
}
