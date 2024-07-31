package me.carscupcake.sbremake.blocks.impl.mithril;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.mining.resources.Mithril;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.Powder;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

import java.util.Set;

public class CyanTerracottaMithril extends MiningBlock {
    private static final Block beforeMat = Block.CYAN_TERRACOTTA;

    public CyanTerracottaMithril() {
        super(beforeMat);
    }

    @Override
    public int blockStrength() {
        return 500;
    }

    @Override
    public int getInstaMineSpeed() {
        return 30_001;
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
        return Set.of(withMiningFortune(ISbItem.get(Mithril.class), 1, player));
    }

    @Override
    public void breakBlock(Pos pos, SkyblockPlayer player, BlockFace face) {
        super.breakBlock(pos, player, face);
        player.addPowder(Powder.MithrilPowder, 1);
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
