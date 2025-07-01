package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.hot;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraChestplateBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraHelmetBaseline;

import java.util.Map;

public class HotAuroraHelmet extends AuroraHelmetBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 202, Stat.Defense, 63, Stat.Intelligence, 239, Stat.AbilityDamage, 20);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzBiNmUyZDQzZWUyNGM5YTI0NmE4MzUxNWY5Yjc0MTQ4NDZmMzE1YWQ5NTQ0MDBjMzhjYTY1Y2RmMDhlOTE5ZSJ9fX0=";
    }
}
