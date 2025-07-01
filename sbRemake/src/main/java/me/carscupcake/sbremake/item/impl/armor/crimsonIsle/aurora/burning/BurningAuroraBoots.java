package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.burning;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraBootsBaseline;

import java.util.Map;

public class BurningAuroraBoots extends AuroraBootsBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 206, Stat.Defense, 64, Stat.Intelligence, 198);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Burning;
    }
}
