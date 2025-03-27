package me.carscupcake.sbremake.item;

import lombok.Getter;

@Getter
public enum ItemRarity {
    COMMON('f', "Common"),
    UNCOMMON('a', "Uncommon"),
    RARE('9', "Rare"),
    EPIC('5', "Epic"),
    LEGENDARY('6', "Legendary"),
    MYTHIC('d', "Mythic"),
    DIVINE('b', "Divine"),
    SPECIAL('c', "Special"),
    VERY_SPECIAL('4', "Very Special"),
    ADMIN('4', "Admin");
    private final char prefix;
    private final String display;

    ItemRarity(char prefix, String display) {
        this.prefix = prefix;
        this.display = display;
    }

    public String getPrefix() {
        return "§" + prefix;
    }

    public ItemRarity next() {
        return next(this);
    }

    public static ItemRarity next(ItemRarity rarity) {
        boolean found = false;
        for (ItemRarity r : ItemRarity.values()) {
            if (found) return r;
            if (rarity == r) found = true;
        }
        return rarity;
    }
}
