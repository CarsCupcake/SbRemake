package me.carscupcake.sbremake.item.impl.other.dungeon.scrolls;

import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import me.carscupcake.sbremake.item.impl.sword.dungeons.NecronBlade;

public class Implosion implements IWitherScroll{
    @Override
    public ItemAbility<?> getBladeAbility() {
        return NecronBlade.IMPLOSION;
    }

    @Override
    public String getId() {
        return "IMPLOSION_SCROLL";
    }

    @Override
    public String getName() {
        return "Implosion";
    }
}
