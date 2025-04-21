package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.burning;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraHelmetBaseline;

import java.util.Map;

public class BurningAuroraHelmet extends AuroraHelmetBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 254, Stat.Defense, 79, Stat.Intelligence, 302, Stat.AbilityDamage, 25);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Burning;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg4NmI0ODRiNzUwYzM2NzEwNzg5MjU4NjkzMGE1ZTUwY2Q1N2U5YmU4NDNiZDNkYjdlYjIxN2NiNzgyZjBhMSJ9fX0=";
    }
}
