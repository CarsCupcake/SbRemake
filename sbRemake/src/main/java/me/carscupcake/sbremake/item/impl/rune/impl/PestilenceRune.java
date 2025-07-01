package me.carscupcake.sbremake.item.impl.rune.impl;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.impl.rune.IRune;
import me.carscupcake.sbremake.item.impl.rune.RuneTicker;
import me.carscupcake.sbremake.item.impl.rune.WeaponRune;

public class PestilenceRune implements WeaponRune {
    @Override
    public RuneTicker<SkyblockEntity> createTicker(SkyblockEntity entity, int level) {
        return new RuneTicker<>() {
            @Override
            public IRune<SkyblockEntity> getRune() {
                return PestilenceRune.this;
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
        return ItemRarity.RARE;
    }

    @Override
    public String getId() {
        return "pestilence";
    }

    @Override
    public String getName() {
        return "§2◆ Pestilence";
    }

    @Override
    public String headValue() {
        return "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYThjNDgxMTM5NWZiZjdmNjIwZjA1Y2MzMTc1Y2VmMTUxNWFhZjc3NWJhMDRhMDEwNDUwMjdmMDY5M2E5MDE0NyJ9fX0=";
    }
}
