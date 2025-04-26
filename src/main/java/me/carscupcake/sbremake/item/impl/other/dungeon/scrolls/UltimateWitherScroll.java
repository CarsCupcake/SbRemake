package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.impl.sword.dungeons.NecronBlade;

public class UltimateWitherScroll implements IWitherScroll {
    @Override
    public ItemAbility<?> getBladeAbility() {
        return NecronBlade.WITHER_IMPACT;
    }

    @Override
    public String getId() {
        return "ULTIMATE_WITHER_SCROLL";
    }

    @Override
    public String getName() {
        return "Ultimate Wither Scroll";
    }
}
