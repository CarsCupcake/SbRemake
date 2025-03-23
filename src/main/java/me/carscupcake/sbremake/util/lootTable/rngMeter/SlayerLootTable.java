package me.carscupcake.sbremake.util.lootTable.rngMeter;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.StringUtils;
import me.carscupcake.sbremake.util.lootTable.ILootTable;

import java.util.*;
import java.util.function.BiFunction;

@Getter
public class SlayerLootTable implements ILootTable<SbItemStack> {
    private double totalWeight;
    private final Map<LootTableType, Double> weightByType = new HashMap<>();
    private final MapList<LootTableType, RngMeterLoot> loot = new MapList<>();
    private final Map<RngMeterLoot, Double> baseChances = new HashMap<>();

    public void addLoot(RngMeterLoot loot) {
        this.loot.add(loot.type(), loot);
    }

    public void calculateWeight(SkyblockPlayer player) {
        totalWeight = 0;
        weightByType.clear();
        for (Map.Entry<LootTableType, List<RngMeterLoot>> lootEntry : loot.entrySet()) {
            for (RngMeterLoot rngMeterLoot : lootEntry.getValue()) {
                double itemWeight = rngMeterLoot.chance(player);
                weightByType.put(lootEntry.getKey(), weightByType.getOrDefault(lootEntry.getKey(), 0d) + itemWeight);
                totalWeight += itemWeight;
            }
        }
    }

    @Override
    public Set<SbItemStack> loot(SkyblockPlayer player) {
        if (baseChances.isEmpty()) {
            calculateWeight(null);
            for (List<RngMeterLoot> loots : loot.values())
                for (RngMeterLoot l : loots)
                    baseChances.put(l, l.type().apply(this, l.weight()));
        }
        Set<SbItemStack> lootSet = new HashSet<>();
        calculateWeight(player);
        for (Map.Entry<LootTableType, List<RngMeterLoot>> lootEntry : loot.entrySet()) {
            Random r = new Random();
            for (RngMeterLoot rngMeterLoot : lootEntry.getValue()) {
                double chance = lootEntry.getKey().apply(this, weightAfterModifiers(player, rngMeterLoot));
                if (chance >= r.nextDouble() || rngMeterLoot.isGuaranteed(player)) {
                    Set<SbItemStack> loot = rngMeterLoot.loot(player);
                    if (player != null) {
                        double magicFind = player.getStat(Stat.MagicFind, true);
                        chance = lootEntry.getKey().apply(this, rngMeterLoot.weight());
                        for (SbItemStack sbItemStack : loot) {
                            if (chance < 0.0003)
                                player.sendMessage("§c§lRNGESUS INCARNATE DROP! §7(" + (sbItemStack.getRarity().getPrefix()) +  (sbItemStack.displayName()) + "§7) §b(+" + (StringUtils.cleanDouble(magicFind)) + "% " + (Stat.MagicFind) + ")");
                            else
                            if (chance < 0.006)
                                player.sendMessage("§d§lPRAY RNGESUS DROP! §7(" + (sbItemStack.getRarity().getPrefix()) +  (sbItemStack.displayName()) + "§7) §b(+" + (StringUtils.cleanDouble(magicFind)) + "% " + (Stat.MagicFind) + ")");
                            else
                            if (chance < 0.03)
                                player.sendMessage("§5§lVERY RARE DROP! §7(" + (sbItemStack.getRarity().getPrefix()) +  (sbItemStack.displayName()) + "§7) §b(+" + (StringUtils.cleanDouble(magicFind)) + "% " + (Stat.MagicFind) + ")");
                            else
                            if (chance < 0.11)
                                player.sendMessage("§9§lRARE DROP! §7(" + (sbItemStack.getRarity().getPrefix()) +  (sbItemStack.displayName()) + "§7) §b(+" + (StringUtils.cleanDouble(magicFind)) + "% " + (Stat.MagicFind) + ")");
                            else if (chance < 0.311)
                                player.sendMessage("§a§lUNCOMMON DROP! §7(" + (sbItemStack.getRarity().getPrefix()) +  (sbItemStack.displayName()) + "§7) §b(+" + (StringUtils.cleanDouble(magicFind)) + "% " + (Stat.MagicFind) + ")");
                        }
                    }
                    lootSet.addAll(loot);
                    break;
                }
            }

        }
        return lootSet;
    }

    private double weightAfterModifiers(SkyblockPlayer player, RngMeterLoot loot) {
        if (player == null) return loot.weight();
        double resultWeight = (player.getSlayers().get(loot.slayer()).getMeter().getSelected() != null && player.getSlayers().get(loot.slayer()).getMeter().getSelected().contains(loot)) ? player.getSlayers().get(loot.slayer()).getMeter().calculateWeight(loot, loot.weight()) : loot.weight();
        return (baseChances.getOrDefault(loot, 1d) <= 0.05) ? (resultWeight * (1 + (player.getStat(Stat.MagicFind) / 100d))) : resultWeight;
    }

    public enum LootTableType implements BiFunction<SlayerLootTable, Double, Double> {
        Token {
            @Override
            public Double apply(SlayerLootTable slayerLootTable, Double aDouble) {
                return 1d;
            }
        },
        Main {
            @Override
            public Double apply(SlayerLootTable slayerLootTable, Double aDouble) {
                return aDouble / (slayerLootTable.totalWeight - slayerLootTable.weightByType.getOrDefault(Extra, 0d));
            }

        },
        Extra {
            @Override
            public Double apply(SlayerLootTable slayerLootTable, Double aDouble) {
                return aDouble / slayerLootTable.totalWeight;
            }

        }
    }
}
