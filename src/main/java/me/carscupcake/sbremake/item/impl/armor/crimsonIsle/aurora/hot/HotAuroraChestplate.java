package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.hot;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraChestplateBaseline;

import java.util.Map;

public class HotAuroraChestplate extends AuroraChestplateBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 290, Stat.Defense, 82, Stat.Intelligence, 158);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }
}
