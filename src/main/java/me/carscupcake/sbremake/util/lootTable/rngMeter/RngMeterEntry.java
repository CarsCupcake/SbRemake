package me.carscupcake.sbremake.util.lootTable.rngMeter;

import me.carscupcake.sbremake.config.KeyClass;

public record RngMeterEntry(String id, RngMeterLoot... loots) {
    public boolean contains(RngMeterLoot loot) {
        for (RngMeterLoot loots : loots)
            if (loots == loot) return true;
        return false;
    }

    public int calculateRequiredXp(double modifier, SlayerLootTable... lootTables) {
        return (int) (modifier / (100 * calculateHighestChance(lootTables)));
    }

    public double calculateHighestChance(SlayerLootTable... lootTables) {
        double chance = 0;
        for (SlayerLootTable lootTable : lootTables) {
            lootTable.calculateWeight(null);
            for (RngMeterLoot l : lootTable.getLoot().get(loots[0].type())) {
                if (contains(l)) {
                    double c = l.type().apply(lootTable, l.chance(null));
                    if (c > chance) chance = c;
                }
            }
        }
        return chance;
    }
}
