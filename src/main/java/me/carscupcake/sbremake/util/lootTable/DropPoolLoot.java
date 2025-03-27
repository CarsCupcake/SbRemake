package me.carscupcake.sbremake.util.lootTable;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public record DropPoolLoot(List<ISbItem> items, int min, int max, double chance, boolean magicFind, boolean petLuck, ItemLoot.MessageBuilder builder) implements LootTable.Loot<SbItemStack> {
    public DropPoolLoot(List<ISbItem> items, int min, int max, double chance) {
        this(items, min, max, chance, chance <= 0.05, false, ItemLoot.NormalMessages.messageBuilder(chance));
    }

    public DropPoolLoot(List<ISbItem> items, int amount, double chance) {
        this(items, amount, amount, chance);
    }

    @Override
    public Set<SbItemStack> loot(SkyblockPlayer player) {
        ISbItem item = items.get(new Random().nextInt(items.size()));
        int amount = (min == max) ? max : (new Random().nextInt(max - min) + min);
        if (builder != null)
            player.sendMessage(builder.message(player,  (item.getRarity().getPrefix()) +  (item.getName()) , amount, magicFind));
        return Set.of(Objects.requireNonNull(item.create().withAmount(amount)));
    }

    @Override
    public double chance(SkyblockPlayer player) {
        return chance * (1d + (((magicFind ? player.getStat(Stat.MagicFind) : 0d) + (petLuck ? player.getStat(Stat.PetLuck) : 0d)) / 100d));
    }
}
