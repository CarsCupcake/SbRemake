package me.carscupcake.sbremake.item;

import lombok.Getter;

@Getter
public enum ItemType {
    None(""),
    Sword(true, "Sword"),
    Helmet(true, "Helmet"),
    Chestplate(true, "Chestplate"),
    Leggings(true, "Leggings"),
    Boots(true, "Boots");
    private final boolean reforgable;
    private final String display;
    ItemType(String display) {
        this(false, display);
    }
    ItemType(boolean reforgable, String display) {
        this.reforgable = reforgable;
        this.display = display;
    }
}
