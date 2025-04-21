package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraBootsBaseline;

import java.util.Map;

public class InfernalAuroraBoots extends AuroraBootsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 328, Stat.Defense, 101, Stat.Intelligence, 315);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }
}
