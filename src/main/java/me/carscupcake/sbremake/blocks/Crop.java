package me.carscupcake.sbremake.blocks;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import javax.annotation.Nullable;
import java.util.*;

public record Crop(Block block, Map<ISbItem, Integer> drop, double xp, @Nullable Stat cropFortune) {
    public static final Set<Crop> crops = Set.of(new Crop(Block.WHEAT, Map.of(ISbItem.get(Material.WHEAT), 1, ISbItem.get(Material.WHEAT_SEEDS), 2), 4, Stat.WheatFortune));
    public Set<SbItemStack> drops(SkyblockPlayer player) {
        Set<SbItemStack> drops = new HashSet<>();
        double fortune = player.getStat(cropFortune) + player.getStat(Stat.FarmingFortune);
        double miningFortune = fortune / 100d;
        long baseMult = (long) miningFortune;
        double chance = miningFortune - baseMult;
        for (Map.Entry<ISbItem, Integer> item : drop.entrySet()) {
            SbItemStack stack = item.getKey().create();
            drops.add(stack.withAmount((int) (item.getValue() * (1 + (new Random().nextDouble() <= chance ? (baseMult + 1) : baseMult)))));
        }
        return drops;
    }
}
