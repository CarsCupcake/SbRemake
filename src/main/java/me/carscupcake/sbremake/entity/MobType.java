package me.carscupcake.sbremake.entity;

import lombok.Getter;

@Getter
public enum MobType {
    Airborne("Airborne", "✈", "§7"),
    Animal("Animal", "☮", "§a"),
    Aquatic("Aquatic", "⚓", "§9"),
    Arcane("Arcane", "♃", "§5"),
    Arthropod("Arthropod", "Ж", "§4"),
    Construct("Construct", "⚙", "§7"),
    Cubic("Cubic", "⚂", "§a"),
    Elusive("Elusive", "♣", "§d"),
    Ender("Ender", "⊙", "§5"),
    Frozen("Frozen", "☃", "§f"),
    Glacial("Glacial", "❄", "§b"),
    Humanoid("Humanoid", "✰", "§e"),
    Infernal("Infernal", "♨", "§4"),
    Magmatic("Magmatic", "♆", "§c"),
    Mythological("Mythological", "✿", "§2"),
    Pest("Pest", "ൠ", "§2"),
    Shielded("Shielded", "⛨", "§e"),
    Skeletal("Skeletal", "\uD83E\uDDB4", "§f"),
    Spooky("Spooky", "☽", "§6"),
    Subterranean("Subterranean", "⛏", "§6"),
    Undead("Undead", "༕", "§2"),
    Wither("Wither", "☠", "§8"),
    Woodland("Woodland", "⸙", "§2");

    private final String name;
    private final String icon;
    private final String color;

    MobType(String name, String icon, String color) {
        this.name = name;
        this.icon = icon;
        this.color = color;
    }
}
