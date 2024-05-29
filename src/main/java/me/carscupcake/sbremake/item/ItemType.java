package me.carscupcake.sbremake.item;

import lombok.Getter;

@Getter
public enum ItemType {
    Sword(true, "Sword");
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
