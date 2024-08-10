package me.carscupcake.sbremake.blocks.impl.ore;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantment;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;

import java.util.Set;

public class GoldOre extends MiningBlock {

    public GoldOre() {
        super(Block.GOLD_ORE);
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
        SbItemStack item = SbItemStack.from(player.getItemInHand(Player.Hand.MAIN));
        return Set.of(withMiningFortune(ISbItem.get((item != null && item.getEnchantmentLevel(NormalEnchantment.SmeltingTouch) > 0) ? Material.GOLD_INGOT : Material.GOLD_ORE), 1, player));
    }

    @Override
    public double getXp() {
        return 6;
    }
}
