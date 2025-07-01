package me.carscupcake.sbremake.blocks.impl;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.Set;

public class Cobblestone extends MiningBlock {
    public Cobblestone() {
        super(Block.COBBLESTONE);
    }

    @Override
    public int blockStrength() {
        return 20;
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
    public Block resetType() {
        return Block.STONE;
    }

    @Override
    public double getXp() {
        return 1;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        return Set.of(withMiningFortune(ISbItem.get(Material.COBBLESTONE), 1, player));
    }
}
