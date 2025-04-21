package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraChestplateBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraLeggingsBaseline;

import java.util.Map;

public class BaseAuroraChestplate extends AuroraChestplateBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 230, Stat.Defense, 65, Stat.Intelligence, 125);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }
}
