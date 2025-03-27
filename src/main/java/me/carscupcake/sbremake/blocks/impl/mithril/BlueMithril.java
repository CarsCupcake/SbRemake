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

public class BlueMithril extends me.carscupcake.sbremake.blocks.impl.mithril.Mithril {
    private static final Block beforeMat = Block.LIGHT_BLUE_WOOL;

    public BlueMithril() {
        super(beforeMat);
    }

    @Override
    public int blockStrength() {
        return 1500;
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
        /*ItemStack item = Items.Mithril();
        item.setAmount(SkyblockRemakeEvents.dropAmount((int)Main.getPlayerStat(player, Stats.MiningFortune), 1));
        return new ArrayList<>(List.of(
                Main.itemUpdater(item, player)
        ));*/

        return Set.of(withMiningFortune(ISbItem.get(Mithril.class), 5, player));
    }

    @Override
    public void breakBlock(Pos pos, SkyblockPlayer player, BlockFace face) {
        super.breakBlock(pos, player, face);
        HotmUpgrade peakOfTheMountain = player.getHotm().getUpgrades().get(18);
        player.addPowder(Powder.MithrilPowder, peakOfTheMountain.getLevel() >= 4 ? 4 : 3);
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
    public Block resetType() {
        return beforeMat;
    }

    @Override
    public double getXp() {
        return 45;
    }
    /*@Override
    public void reset(){
        block.setType(beforeMat);
    }*/
}
