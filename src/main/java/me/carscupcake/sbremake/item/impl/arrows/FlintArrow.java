package me.carscupcake.sbremake.item.impl.arrows;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.player.SkyblockPlayer;

public class FlintArrow implements SkyblockArrow{
    @Override
    public void onEntityHit(SkyblockEntity entity, SkyblockPlayer shooter) {

    }

    @Override
    public String getId() {
        return "ARROW";
    }

    @Override
    public String getName() {
        return "Flint Arrow";
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.COMMON;
    }

    @Override
    public double getStat(Stat stat) {
        if (stat == Stat.Damage) return 1;
        return SkyblockArrow.super.getStat(stat);
    }
}
