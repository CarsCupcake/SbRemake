package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.infernal;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraChestplateBaseline;

import java.util.Map;

public class InfernalAuroraChestplate extends AuroraChestplateBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 580, Stat.Defense, 164, Stat.Intelligence, 315);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Infernal;
    }
}
