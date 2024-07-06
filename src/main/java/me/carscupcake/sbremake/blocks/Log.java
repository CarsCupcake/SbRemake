package me.carscupcake.sbremake.blocks;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.predicate.BlockTypeFilter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public record Log(Block block, double xp) {
    public static final Set<Log> logs = Set.of(new Log(Block.OAK_LOG, 6d), new Log(Block.OAK_WOOD, 6d));
    public SbItemStack drops(SkyblockPlayer player) {
        SbItemStack item = SbItemStack.base(Objects.requireNonNull(block.registry().material()));
        double miningFortune = player.getStat(Stat.ForagingFortune) / 100d;
        long baseMult = (long) miningFortune;
        double chance = miningFortune - baseMult;
        if (new Random().nextDouble() <= chance) baseMult++;
        return item.withAmount((int) (1 + baseMult));
    }
}
