package me.carscupcake.sbremake.player.hotm;

import lombok.Getter;

@Getter
public enum Powder {
    MithrilPowder("mithril", "Mithril", "§2"),
    GemstonePowder("gemstone", "Gemstone", "§d"),
    GlacialPowder("glacial", "Glacial", "§b");

    private final String id;
    private final String name;
    private final String color;

    Powder(String id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }
}
