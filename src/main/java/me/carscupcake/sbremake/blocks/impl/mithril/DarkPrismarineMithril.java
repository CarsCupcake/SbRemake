package me.carscupcake.sbremake.blocks.impl.mithril;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.mining.resources.Mithril;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.instance.block.Block;

import java.util.Set;

public class DarkPrismarineMithril extends MiningBlock {
    private static final Block beforeMat = Block.DARK_PRISMARINE;

    public DarkPrismarineMithril() {
        super(beforeMat, SkyblockWorld.DwarvenMines);
    }

    @Override
    public int blockStrength() {
        return 800;
    }

    @Override
    public int getInstaMineSpeed() {
        return 48_001;
    }

    @Override
    public int regenTime() {
        return 8 * 20;
    }

    @Override
    public int getBreakingPower() {
        return 4;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        return Set.of(withMiningFortune(ISbItem.get(Mithril.class), 2, player));
    }

    @Override
    public Block resetType() {
        return beforeMat;
    }

    @Override
    public double miningXp() {
        return 45;
    }
}
