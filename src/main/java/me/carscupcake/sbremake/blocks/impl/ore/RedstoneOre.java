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

import java.util.Random;
import java.util.Set;

public class RedstoneOre extends MiningBlock {

    public RedstoneOre() {
        super(Block.REDSTONE_ORE);
    }

    @Override
    public int blockStrength() {
        return 30;
    }

    @Override
    public int getBreakingPower() {
        return 3;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        SbItemStack item = player.getSbItemInHand(PlayerHand.MAIN);
        boolean b = item != null && item.getEnchantmentLevel(NormalEnchantments.SilkTouch) > 0;
        return Set.of(withMiningFortune(ISbItem.get((b) ? Material.REDSTONE_ORE : Material.REDSTONE), b ? 1 : (4 + new Random().nextInt()), player));
    }

    @Override
    public double getXp() {
        return 9;
    }
}
