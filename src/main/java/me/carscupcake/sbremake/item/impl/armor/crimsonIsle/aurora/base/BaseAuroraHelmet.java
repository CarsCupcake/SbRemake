package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraChestplateBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.aurora.AuroraHelmetBaseline;

import java.util.Map;

public class BaseAuroraHelmet extends AuroraHelmetBaseline {
    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 160, Stat.Defense, 50, Stat.Intelligence, 190, Stat.AbilityDamage, 16);
    }

    @Override
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }

    @Override
    public String value() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzYzZDJmZTkzODgxYjRmMjZjYmUxZGQzYjA5ZGE3Y2M0OGRiY2RjNTY4ZDE5ODUyYWQ2MzVkNWQxNjg1OTYxMSJ9fX0=";
    }
}
