package me.carscupcake.sbremake.util.lootTable;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import java.util.Objects;
import java.util.Random;
import java.util.Set;

public record ItemLoot(ISbItem item, int min, int max, double chance, boolean magicFind, boolean petLuck, MessageBuilder builder) implements LootTable.Loot<SbItemStack> {
    public ItemLoot(ISbItem item, int min, int max, double chance) {
        this(item, min, max, chance, chance <= 0.05, false, NormalMessages.messageBuilder(chance));
    }

    public ItemLoot(ISbItem item, int amount, double chance) {
        this(item, amount, amount, chance);
    }
    @Override
    public Set<SbItemStack> loot(SkyblockPlayer player) {
        int amount = (min == max) ? max : (new Random().nextInt(max - min) + min);
        if (builder != null)
            player.sendMessage(builder.message(player, STR."\{item.getRarity().getPrefix()}\{item.getName()}", amount, magicFind));
        return Set.of(Objects.requireNonNull(item.create().withAmount(amount)));
    }

    @Override
    public double chance(SkyblockPlayer player) {
        return chance * (1d + (((magicFind ? player.getStat(Stat.MagicFind) : 0d) + (petLuck ? player.getStat(Stat.PetLuck) : 0d)) / 100d));
    }

    public interface MessageBuilder {
        String message(SkyblockPlayer player, String itemName, int amount, boolean magicFind);
    }

    public enum NormalMessages implements MessageBuilder {
        Rare {
            @Override
            public String message(SkyblockPlayer player, String itemName, int amount, boolean magicFind) {
                return STR."§6§lRARE DROP! \{itemName} \{(amount != 1) ? STR."§8\{amount}x " : ""}\{magicFind ? STR."§b(+\{player.getStat(Stat.MagicFind)} \{Stat.MagicFind})" : ""}";
            }
        },
        Legendary {
            @Override
            public String message(SkyblockPlayer player, String itemName, int amount, boolean magicFind) {
                return STR."§6§lLEGENDARY DROP! \{itemName} \{(amount != 1) ? STR."§8\{amount}x " : ""}\{magicFind ? STR."§b(+\{player.getStat(Stat.MagicFind)} \{Stat.MagicFind})" : ""}";
            }
        },
        RNGesus {
            @Override
            public String message(SkyblockPlayer player, String itemName, int amount, boolean magicFind) {
                return STR."§d§lRNGesus DROP! \{itemName} \{(amount != 1) ? STR."§8\{amount}x " : ""}\{magicFind ? STR."§b(+\{player.getStat(Stat.MagicFind)} \{Stat.MagicFind})" : ""}";
            }
        };

        public static MessageBuilder messageBuilder(double chance) {
            if (chance <= 0.0001) return RNGesus;
            else if (chance <= 0.001) return Legendary;
            else if (chance <= 0.01) return Rare;
            return null;
        }
    }
}
