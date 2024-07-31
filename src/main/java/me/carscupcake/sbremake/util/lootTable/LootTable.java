package me.carscupcake.sbremake.util.lootTable;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class LootTable<T> {
    private final List<Loot<T>> loot = new ArrayList<>();
    private boolean singleLoot = false;

    public LootTable<T> setSingleLoot() {
        singleLoot = true;
        return this;
    }

    public LootTable<T> addLoot(Loot<T> loot) {
        this.loot.add(loot);
        return this;
    }

    public LootTable<T> addLootTable(LootTable<T> loot, double chance) {
        return addLoot(new LootTableLoot<>(loot, chance, true));
    }

    public Set<T> loot(SkyblockPlayer player) {
        Set<T> set = new HashSet<>();
        Random r = new Random();
        if (singleLoot) {
            double sum = 0;
            HashMap<Loot<T>, Double> table = new HashMap<>();
            for (Loot<T> l : loot) {
                double d = l.chance(player);
                sum += d;
                table.put(l, d);
            }
            double rand = r.nextDouble(sum);
            for (Map.Entry<Loot<T>, Double> entry : table.entrySet()) {
                if (entry.getValue() >= rand)
                    return entry.getKey().loot(player);
                rand -= entry.getValue();
            }
        }
        for (Loot<T> loot1 : loot) {
            if (loot1.chance(player) >= r.nextDouble()) {
                set.addAll(loot1.loot(player));
                if (singleLoot) break;
            }
        }
        return set;
    }

    public static record LootTableLoot<T>(LootTable<T> table, double chance, boolean magicFind) implements Loot<T> {

        public LootTableLoot(LootTable<T> table, double chance) {
            this(table, chance, chance <= 0.05);
        }

        @Override
        public Set<T> loot(SkyblockPlayer player) {
            return table.loot(player);
        }

        @Override
        public double chance(SkyblockPlayer player) {
            return chance * (1d + (player.getStat(Stat.MagicFind) / 100d));
        }
    }


    public interface Loot<T> {
        Set<T> loot(SkyblockPlayer player);

        double chance(SkyblockPlayer player);
    }
}
