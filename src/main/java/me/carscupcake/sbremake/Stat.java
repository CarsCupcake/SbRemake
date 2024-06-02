package me.carscupcake.sbremake;

import lombok.Getter;

@Getter
public enum Stat {
    Health("Health", "❤", "§c", false, 100d, -1d),
    Defense("Defense", "❈", "§a", false),
    Intelligence("Intelligence", "✎", "§b", false),
    HealthRegen("Health Regen", "❣", "§c", false, 100, -1),
    Vitality("Vitality", "♨", "§4", false, 100, -1);
    private final String name;
    private final String symbol;
    private final String prefix;
    private final boolean percentValue;
    private final double baseValue;
    private final double maxValue;
    Stat(String name, String symbol, String prefix, boolean percentValue, double baseValue, double maxValue) {
        this.name = name;
        this.symbol = symbol;
        this.prefix = prefix;
        this.percentValue = percentValue;
        this.baseValue = baseValue;
        this.maxValue = maxValue;
    }
    Stat(String name, String symbol, String prefix, boolean isPercentValue) {
        this(name, symbol, prefix, isPercentValue, 0, -1);
    }
}
