package me.carscupcake.sbremake.blocks.impl.mithril;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.blocks.impl.Titanium;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.hotm.impl.TitaniumInsanium;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

import java.util.Random;

public abstract class Mithril extends MiningBlock {
    public Mithril(Block block) {
        super(block);
    }

    @Override
    public void breakBlock(Pos pos, SkyblockPlayer player, BlockFace face) {
        Instance instance = player.getInstance();
        instance.setBlock(pos, blockIfBroken());
        TitaniumInsanium titaniumInsanium = player.getHotm().getUpgrade(TitaniumInsanium.class);
        double titaniumChance = (titaniumInsanium.getLevel() > 0 && titaniumInsanium.isEnabled()) ? (titaniumInsanium.reward(titaniumInsanium.getLevel()) / 100) : 0.005;
        if (titaniumChance >= new Random().nextDouble())
            Titanium.setBlock(player.getInstance(), new BlockVec(pos), this);
        else
            new TaskScheduler() {
                @Override
                public void run() {
                    reset(instance, pos);
                }
            }.delayTask(regenTime());
        dropItems(player, pos, face);
        player.getSkill(getSkill()).addXp(getXp());
        instance.playSound(breakingSound(), pos);
    }
}
