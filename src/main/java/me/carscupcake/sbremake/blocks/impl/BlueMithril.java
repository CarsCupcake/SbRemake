package me.carscupcake.sbremake.blocks.impl;

import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BlueMithril extends MiningBlock {
    private static final Block beforeMat = Block.LIGHT_BLUE_WOOL;

    public BlueMithril() {
        super(beforeMat);
    }

    @Override
    public int blockStrength() {
        return 1500;
    }
    @Override
    public int getInstaMineSpeed() {
        return 90000;
    }
    @Override
    public int regenTime(){
        return 8*20;
    }

    @Override
    public int getBreakingPower() {
        return 4;
    }

    @Override
    public Set<SbItemStack> getDrops(SkyblockPlayer player) {
        /*ItemStack item = Items.Mithril();
        item.setAmount(SkyblockRemakeEvents.dropAmount((int)Main.getPlayerStat(player, Stats.MiningFortune), 1));
        return new ArrayList<>(List.of(
                Main.itemUpdater(item, player)
        ));*/
        return new HashSet<>();
    }
    /*@Override
    public void breakBlock(Block b, SkyblockPlayer player){
        this.block = b;
        beforeMat = b.getType();
        double chance = player.titaniumchance / 100;
        double i = new Random().nextDouble();
        boolean isTitanium = i <= chance;

        if(isTitanium){
            b.setType(Material.POLISHED_DIORITE);
            new TitanumHandler(b, beforeMat);
        }else super.breakBlock(b,player);
        dropItems(player);
    }*/
    @Override
    public Block resetType(){return beforeMat;}
    /*@Override
    public void reset(){
        block.setType(beforeMat);
    }*/
}
