package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraChestplateBaseline;

import java.util.Map;

public class FieryAuroraChestplate extends AuroraChestplateBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 460, Stat.Defense, 130, Stat.Intelligence, 250);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Fiery;
    }
}
