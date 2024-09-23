package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonHelmetBaseline;

import java.util.Map;

public class FieryCrimsonHelmet extends CrimsonHelmetBaseline {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Fiery;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTFmMGM3YWZmZjE3ODI0NjVkOGNkYjVlYmEyNjFiNjU0MjNhN2EwNzEyZWUzYTRjNTcyYzMzZjk0YzY4YzU1In19fQ==";
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 320, Stat.Defense, 100, Stat.Strength, 60, Stat.Intelligence, 30, Stat.CritDamage, 40);
    }
}
