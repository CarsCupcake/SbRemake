package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.impl.sword.dungeons.NecronBlade;

public class ShadowWarp implements IWitherScroll {
    @Override
    public ItemAbility<?> getBladeAbility() {
        return NecronBlade.SHADOW_WARP;
    }

    @Override
    public String getId() {
        return "SHADOW_WARP_SCROLL";
    }

    @Override
    public String getName() {
        return "Shadow Warp";
    }
}
