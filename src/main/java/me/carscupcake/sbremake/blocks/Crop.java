package me.carscupcake.sbremake.blocks;

import kotlin.Pair;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import javax.annotation.Nullable;
import java.util.*;

public record Crop(Block block, Map<ISbItem, Pair<Integer, Integer>> drop, double xp, @Nullable Stat cropFortune) {
    public static final Set<Crop> crops = Set.of(new Crop(Block.WHEAT, Map.of(ISbItem.get(Material.WHEAT), new Pair<>(1, 1), ISbItem.get(Material.WHEAT_SEEDS), new Pair<>(2, 2)), 4, Stat.WheatFortune),
            new Crop(Block.CARROTS, Map.of(ISbItem.get(Material.CARROT), new Pair<>(2, 3)), 4, Stat.CarrotFortune), new Crop(Block.POTATOES, Map.of(ISbItem.get(Material.POTATO), new Pair<>(2, 5)), 4, Stat.PotatoFortune),
            new Crop(Block.MELON, Map.of(ISbItem.get(Material.MELON_SLICE), new Pair<>(3, 7)), 4, Stat.MelonFortune), new Crop(Block.CARVED_PUMPKIN, Map.of(ISbItem.get(Material.PUMPKIN), new Pair<>(1, 1)), 4, Stat.PumpkinFortune));
    public Set<SbItemStack> drops(SkyblockPlayer player) {
        Set<SbItemStack> drops = new HashSet<>();
        double fortune = player.getStat(cropFortune) + player.getStat(Stat.FarmingFortune);
        double miningFortune = fortune / 100d;
        long baseMult = (long) miningFortune;
        double chance = miningFortune - baseMult;
        for (Map.Entry<ISbItem, Pair<Integer, Integer>> item : drop.entrySet()) {
            SbItemStack stack = item.getKey().create();
            int delta = item.getValue().getSecond() - item.getValue().getFirst();
            int base = delta == 0 ? item.getValue().getFirst() : (item.getValue().getFirst() + new Random().nextInt(delta + 1));
            drops.add(stack.withAmount((int) (base * (1 + (new Random().nextDouble() <= chance ? (baseMult + 1) : baseMult)))));
        }
        return drops;
    }
}
