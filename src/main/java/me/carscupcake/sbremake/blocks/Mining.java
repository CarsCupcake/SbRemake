package me.carscupcake.sbremake.blocks;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;

public class Mining extends TaskScheduler {
    private final SkyblockPlayer player;
    private final Pos pos;
    private final MiningBlock miningBlock;
    private final int totalTicks;
    private int ticks;
    private final BlockFace face;

    public static void make(SkyblockPlayer player, Pos pos, BlockFace face) {
        MiningBlock miningBlock = null;
        Block block = player.getInstance().getBlock(pos);
        for (MiningBlock m : player.getWorldProvider().ores(pos))
            if (m.getBlock() == block) {
                miningBlock = m;
                break;
            }
        if (miningBlock == null) return;
        int breakingPower = (int) player.getStat(Stat.BreakingPower);
        if (breakingPower < miningBlock.getBreakingPower()) return;
        int ticks;
        double speed = player.getStat(Stat.MiningSpeed);
        if (miningBlock.isInstantBreak((int) speed)) {
            miningBlock.breakBlock(pos, player, face);
            new TaskScheduler() {
                @Override
                public void run() {
                    make(player, pos, face);
                }
            }.delayTask(1);
            return;
        }
        if (speed >= miningBlock.getSoftCap()) {
            if (speed >= miningBlock.getInstaMineSpeed()) ticks = 1;
            else ticks = 4;
        } else ticks = miningBlock.getMiningTicks(player);
        new Mining(player, pos, miningBlock, ticks, face);
    }

    public Mining(SkyblockPlayer player, Pos pos, MiningBlock miningBlock, int ticks, BlockFace face) {
        this.player = player;
        this.pos = pos;
        this.miningBlock = miningBlock;
        this.ticks = ticks;
        this.totalTicks = ticks;
        this.face = face;
        repeatTask(1, 1);
        player.setBlockBreakScheduler(this);
    }

    @Override
    public void run() {
        ticks--;
        if (ticks <= 0) {
            cancel();
            miningBlock.breakBlock(pos, player, face);
             make(player, pos, face);
            return;
        }
        byte newBreakStage = getBlockBreakStage();
        BlockBreakAnimationPacket animationPacket = new BlockBreakAnimationPacket(player.getEntityId() + 1, pos, newBreakStage);
        player.sendPacket(animationPacket);

    }

    @Override
    public synchronized void cancel() {
        BlockBreakAnimationPacket animationPacket = new BlockBreakAnimationPacket(player.getEntityId() + 1, pos, (byte) 10);
        player.sendPacket(animationPacket);
        player.setBlockBreakScheduler(null);
        super.cancel();
    }

    public byte getBlockBreakStage() {
        double result = ((double) ticks / (double) totalTicks) * 10;
        return (byte) (9 - result);
    }
}
