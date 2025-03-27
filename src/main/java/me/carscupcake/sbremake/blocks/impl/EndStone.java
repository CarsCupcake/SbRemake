package me.carscupcake.sbremake.blocks.impl;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.entity.impl.end.Endermite;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.Material;

import java.util.Random;
import java.util.Set;

public class EndStone extends MiningBlock {
    private static final Random RANDOM = new Random();
    public EndStone() {
        super(Block.END_STONE);
    }

    @Override
    public int blockStrength() {
        return 30;
    }

    @Override
    public int getBreakingPower() {
        return 1;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        return Set.of(withMiningFortune(ISbItem.get(Material.END_STONE), 1, player));
    }

    @Override
    public double getXp() {
        return 3;
    }

    @Override
    public void breakBlock(Pos pos, SkyblockPlayer player, BlockFace face) {
        super.breakBlock(pos, player, face);
        if (RANDOM.nextDouble() <= 0.05) {
            var endermite = new Endermite(pos.y() <= 74);
            endermite.setInstance(player.getInstance(), pos.add(face.toDirection().vec().normalize()));
        }
    }
}
