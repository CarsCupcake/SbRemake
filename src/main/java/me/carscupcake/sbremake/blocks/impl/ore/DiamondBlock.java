package me.carscupcake.sbremake.blocks.impl.ore;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.Set;

public class DiamondBlock extends MiningBlock {

    public DiamondBlock() {
        super(Block.DIAMOND_BLOCK);
    }

    @Override
    public int blockStrength() {
        return 50;
    }

    @Override
    public int getBreakingPower() {
        return 3;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        SbItemStack item = player.getSbItemInHand(PlayerHand.MAIN);
        boolean b = item != null && item.getEnchantmentLevel(NormalEnchantments.SilkTouch) > 0;
        return Set.of(withMiningFortune(ISbItem.get((b) ? Material.DIAMOND_BLOCK : Material.DIAMOND), b ? 1 : 9, player));
    }

    @Override
    public double getXp() {
        return 15;
    }
}
