package me.carscupcake.sbremake.entity.impl.dungeon;

import lombok.Getter;

@Getter
public enum DungeonMobModifier {
    Flaming("§c"),
    Stormy("§9"),
    Speed("§f"),
    Fortified("§8"),
    Healthy("§a");
    private final String color;
    DungeonMobModifier(String color) {
        this.color = color;
    }
}
