package me.carscupcake.sbremake.item.impl.rune.impl;

import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.impl.rune.ArmorRune;
import me.carscupcake.sbremake.item.impl.rune.IRune;
import me.carscupcake.sbremake.item.impl.rune.RuneTicker;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class SnakeRune implements ArmorRune {
    @Override
    public RuneTicker<SkyblockPlayer> createTicker(SkyblockPlayer entity, int level) {
        return new RuneTicker<>() {
            @Override
            public IRune<SkyblockPlayer> getRune() {
                return SnakeRune.this;
            }

            @Override
            public void tick() {

            }
        };
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
        return "snake";
    }

    @Override
    public String getName() {
        return "§a◆ Snake";
    }

    @Override
    public String headValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmM0YTY1YzY4OWIyZDM2NDA5MTAwYTYwYzJhYjhkM2QwYTY3Y2U5NGVlYTNjMWY3YWM5NzRmZDg5MzU2OGI1ZCJ9fX0=";
    }

    @Override
    public ItemType[] allowedArmor() {
        return new ItemType[]{ItemType.Boots};
    }
}
