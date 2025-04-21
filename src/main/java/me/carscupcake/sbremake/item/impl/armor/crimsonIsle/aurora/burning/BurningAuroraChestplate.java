package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.burning;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraChestplateBaseline;

import java.util.Map;

public class BurningAuroraChestplate extends AuroraChestplateBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 365, Stat.Defense, 103, Stat.Intelligence, 198);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Burning;
    }
}
