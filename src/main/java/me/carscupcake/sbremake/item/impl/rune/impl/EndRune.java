package me.carscupcake.sbremake.item.impl.rune.impl;

import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.impl.rune.BowRune;
import me.carscupcake.sbremake.item.impl.rune.RuneTicker;
import me.carscupcake.sbremake.player.SkyblockPlayerArrow;

public class EndRune implements BowRune {
    private final ItemType[] ARMOR = {ItemType.Boots};

    @Override
    public RuneTicker<SkyblockPlayerArrow> createTicker(SkyblockPlayerArrow entity, int level) {
        return null;
    }

    @Override
    public int maxRuneLevel() {
        return 3;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public String getId() {
        return "end";
    }

    @Override
    public String getName() {
        return "§5◆ End";
    }

    @Override
    public String headValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2IxMWZiOTBkYjdmNTdiZWI0MzU5NTQwMTNiMWM3ZWY3NzZjNmJkOTZjYmYzMzA4YWE4ZWJhYzI5NTkxZWJiZCJ9fX0=";
    }
}
