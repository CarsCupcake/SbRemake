package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraLeggingsBaseline;

import java.util.Map;

public class InfernalAuroraLeggings extends AuroraLeggingsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 517, Stat.Defense, 139, Stat.Intelligence, 315);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }
}
