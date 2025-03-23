package me.carscupcake.sbremake.blocks.impl.mithril;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.mining.resources.Mithril;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.HotmUpgrade;
import me.carscupcake.sbremake.player.hotm.Powder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

import java.util.Set;

public class PrismarineMithril extends me.carscupcake.sbremake.blocks.impl.mithril.Mithril {
    private static final Block beforeMat = Block.PRISMARINE;

    public PrismarineMithril() {
        super(beforeMat);
    }

    @Override
    public int blockStrength() {
        return 800;
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
    public void breakBlock(Pos pos, SkyblockPlayer player, BlockFace face) {
        super.breakBlock(pos, player, face);
        HotmUpgrade peakOfTheMountain = player.getHotm().getUpgrades().get(18);
        player.addPowder(Powder.MithrilPowder, peakOfTheMountain.getLevel() >= 4 ? 3 : 2);
    }

    @Override
    public Block resetType() {
        return beforeMat;
    }

    @Override
    public double getXp() {
        return 45;
    }
}
