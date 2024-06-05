package me.carscupcake.sbremake.item;

import lombok.Getter;

@Getter
public enum ItemType {
    None(""),
    Sword(true, true, "Sword"),
    Longsword(true, true, "Longsword"),
    Helmet(true, false, "Helmet"),
    Chestplate(true, false, "Chestplate"),
    Leggings(true, false, "Leggings"),
    Boots(true, false, "Boots"),
    Arrow(false, false, "Arrow"),
    Bow(false, true, "Bow");
    private final boolean reforgable;
    private final boolean statsInMainhand;
    private final String display;
    ItemType(String display) {
        this(false, true, display);
    }
    ItemType(boolean reforgable, boolean statInMainHand, String display) {
        this.statsInMainhand = statInMainHand;
        this.reforgable = reforgable;
        this.display = display;
    }
}
