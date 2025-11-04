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
    Bow(true, false, "Bow"),
    Pickaxe(true, true, "Pickaxe"),
    Drill(true, true, "Drill"),
    Shovel(true, true, "Shovel"),
    Axe(true, true, "Axe"),
    FishingRod(true, true, "Fishing Rod"),
    Pet(false, false, "Pet"),
    Potion(false, false, "Potion"),
    Rune(false, false, "Rune"),
    Cloak(true, false, "Cloak"),
    Belt(true, false, "Belt"),
    Gauntlet(true, false, "Gauntlet"),
    Necklace(true, false, "Necklace"),
    Accessory(false, false, "Accessory"),
    Salt(false, true, "Salt"),
    ReforgeStone(false, true, "Reforge Stone"),
    Minion(false, true, "Minion"),
    ArrowPoison(false, false, "Arrow Poison"),
    Wand(false, true, "Wand"),
    AttributeShard(false, true, "Attribute Shard");

    public static final ItemType[] SWORDS = {Sword, Longsword};
    public static final ItemType[] ARMOR = {Helmet, Chestplate, Leggings, Boots};

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
