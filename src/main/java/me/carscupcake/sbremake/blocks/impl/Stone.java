package me.carscupcake.sbremake.blocks.impl;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.Set;

public class Stone extends MiningBlock {
    public Stone() {
        super(Block.STONE);
    }

    @Override
    public int blockStrength() {
        return 15;
    }

    @Override
    public int getInstaMineSpeed() {
        return blockStrength() * 30;
    }

    @Override
    public int getBreakingPower() {
        return 1;
    }

    @Override
    public Block blockIfBroken() {
        return Block.COBBLESTONE;
    }

    @Override
    public double getXp() {
        return 1;
    }

    @Override
    public void reset(Instance instance, Pos block) {
        if (instance.getBlock(block) == Block.BEDROCK) return;
        super.reset(instance, block);
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        return Set.of(SbItemStack.base(Material.COBBLESTONE));
    }
}
