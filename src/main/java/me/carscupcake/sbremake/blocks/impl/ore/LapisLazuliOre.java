package me.carscupcake.sbremake.blocks.impl.ore;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.Random;
import java.util.Set;

public class LapisLazuliOre extends MiningBlock {

    public LapisLazuliOre() {
        super(Block.LAPIS_ORE);
    }

    @Override
    public int blockStrength() {
        return 30;
    }

    @Override
    public int getBreakingPower() {
        return 2;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        SbItemStack item = SbItemStack.from(player.getItemInHand(PlayerHand.MAIN));
        boolean b = item != null && item.getEnchantmentLevel(NormalEnchantment.SilkTouch) > 0;
        return Set.of(withMiningFortune(ISbItem.get(b ? Material.LAPIS_ORE : Material.LAPIS_LAZULI), b ? 1 : (4 + new Random().nextInt(6)), player));
    }

    @Override
    public double getXp() {
        return 7;
    }
}
