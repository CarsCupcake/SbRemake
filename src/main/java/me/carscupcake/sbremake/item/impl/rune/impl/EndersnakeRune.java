package me.carscupcake.sbremake.item.impl.rune.impl;

import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.impl.rune.ArmorRune;
import me.carscupcake.sbremake.item.impl.rune.RuneTicker;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class EndersnakeRune implements ArmorRune {
    private final ItemType[] ARMOR = {ItemType.Boots};
    @Override
    public ItemType[] allowedArmor() {
        return ARMOR;
    }

    @Override
    public RuneTicker<SkyblockPlayer> createTicker(SkyblockPlayer entity, int level) {
        return null;
    }

    @Override
    public int maxRuneLevel() {
        return 3;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public String getId() {
        return "endersnake";
    }

    @Override
    public String getName() {
        return "§5◆ Endersnake";
    }

    @Override
    public String headValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNhOWFjYmI3ZDNkNDliMWQ1NGQyNjExMTEwNGQwZGE1N2Q4YjRhYjM3ODg1YjRiYmQyNDBhYzcxMDc0Y2FkMiJ9fX0=";
    }
}
