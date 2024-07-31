package me.carscupcake.sbremake.util.lootTable;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.*;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.tag.Tag;

import java.util.Random;
import java.util.Set;

public record CoinLoot(double min, double max) implements LootTable.Loot<SbItemStack> {
    public CoinLoot(double amount) {
        this(amount, amount);
    }
    @Override
    public Set<SbItemStack> loot(SkyblockPlayer player) {
        ISbItem item;
        double amount = (min == max) ? max : new Random().nextDouble(max - min) + min;
        if (amount > 5_000)
            item = ISbItem.get(CoinItem5000.class);
        else if (amount > 2_000)
            item = ISbItem.get(CoinItem2000.class);
        else if (amount > 1_000)
            item = ISbItem.get(CoinItem1000.class);
        else if (amount > 100)
            item = ISbItem.get(CoinItem100.class);
        else if (amount > 10)
            item = ISbItem.get(CoinItem10.class);
        else item = ISbItem.get(CoinItem1.class);
        SbItemStack sb = item.create();
        return Set.of(SbItemStack.from(sb.item().withTag(Tag.Double("coinamount"), amount)));
    }

    @Override
    public double chance(SkyblockPlayer player) {
        return 1;
    }
}
