package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.fiery;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraHelmetBaseline;

import java.util.Map;

public class FieryAuroraHelmet extends AuroraHelmetBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 320, Stat.Defense, 100, Stat.Intelligence, 380, Stat.AbilityDamage, 32);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Hot;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg1NWFhYmFmYTQ2MWQzNWI4NjQyOTFkNDk0Y2EzNmEyZjQ2MzA3Y2VhZGFkM2EyODc3NDA1NGY4Njc4OGU4In19fQ==";
    }
}
