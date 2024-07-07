package me.carscupcake.sbremake.blocks.impl.ore;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.Set;

public class CoalOre extends MiningBlock {

    public CoalOre() {
        super(Block.COAL_ORE);
    }

    @Override
    public int blockStrength() {
        return 30;
    }

    @Override
    public int getInstaMineSpeed() {
        return 1_801;
    }

    @Override
    public int getBreakingPower() {
        return 1;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        return Set.of(withMiningFortune(ISbItem.get(Material.COAL), 1, player));
    }

    @Override
    public double miningXp() {
        return 5;
    }
}
