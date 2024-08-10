package me.carscupcake.sbremake.blocks.impl;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.Set;

public class Titanium extends MiningBlock {
    public static final HashMap<BlockVec, Block> mithrilBlocks = new HashMap<>();

    public Titanium() {
        super(Block.POLISHED_DIORITE);
    }

    @Override
    public int blockStrength() {
        return 2_000;
    }

    @Override
    public int getBreakingPower() {
        return 5;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        return Set.of(withMiningFortune(ISbItem.get(me.carscupcake.sbremake.item.impl.other.mining.resources.Titanium.class), 2, player));
    }

    @Override
    public void reset(Instance instance, Pos block) {
        BlockVec vec = new BlockVec(block);
        instance.setBlock(vec, mithrilBlocks.get(vec));
        mithrilBlocks.remove(vec);
    }

    @Override
    public double getXp() {
        return 100;
    }

    @Override
    public int regenTime() {
        return 8 * 20;
    }

    public static void setBlock(Instance instance, BlockVec vec, MiningBlock previous) {
        mithrilBlocks.put(vec, previous.getBlock());
        instance.setBlock(vec, Block.POLISHED_DIORITE);
    }
}
