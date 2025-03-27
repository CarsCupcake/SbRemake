package me.carscupcake.sbremake.item.modifiers;

import me.carscupcake.sbremake.item.ItemRarity;

public record RarityStat(double common, double uncommon, double rare, double epic, double legendary, double mythic,
                         double divine) {
    public RarityStat(double common, double uncommon, double rare, double epic, double legendary, double mythic) {
        this(common, uncommon, rare, epic, legendary, mythic, mythic);
    }

    public RarityStat(double common, double uncommon, double rare, double epic, double legendary) {
        this(common, uncommon, rare, epic, legendary, legendary);
    }

    public RarityStat(double common, double uncommon, double rare, double epic) {
        this(common, uncommon, rare, epic, epic);
    }

    public RarityStat(double common, double uncommon, double rare) {
        this(common, uncommon, rare, rare);
    }

    public RarityStat(double common, double uncommon) {
        this(common, uncommon, uncommon);
    }

    public RarityStat(double value) {
        this(value, value);
    }

    public double fromRarity(ItemRarity rarity) {
        return switch (rarity) {
            case null -> 0;
            case COMMON -> common;
            case UNCOMMON -> uncommon;
            case RARE -> rare;
            case EPIC -> epic;
            case LEGENDARY -> legendary;
            case MYTHIC -> mythic;
            default -> divine;
        };
    }
}
