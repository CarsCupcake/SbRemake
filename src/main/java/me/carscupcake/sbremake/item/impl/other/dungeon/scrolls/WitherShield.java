package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.impl.sword.dungeons.NecronBlade;

public class WitherShield implements IWitherScroll{
    @Override
    public ItemAbility<?> getBladeAbility() {
        return NecronBlade.WITHER_SHIELD;
    }

    @Override
    public String getId() {
        return "WITHER_SHIELD_SCROLL";
    }

    @Override
    public String getName() {
        return "Wither Shield";
    }
}
