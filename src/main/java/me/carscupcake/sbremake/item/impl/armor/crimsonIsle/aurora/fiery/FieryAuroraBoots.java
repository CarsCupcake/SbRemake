package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraBootsBaseline;

import java.util.Map;

public class FieryAuroraBoots extends AuroraBootsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 260, Stat.Defense, 80, Stat.Intelligence, 250);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Fiery;
    }
}
