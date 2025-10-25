package me.carscupcake.sbremake.item.minion;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockBreakAnimationPacket;
import net.minestom.server.network.packet.server.play.WorldEventPacket;

import java.util.*;

public class MiningMinion extends AbstractMinion {
    private final AbstractMiningMinionData minion;

    public MiningMinion(int level, AbstractMiningMinionData base, Instance instance, Pos location, String minionid, UUID player) {
        super(level, base, instance, location, minionid, player);
        minion = base;
    }

    @Override
    public void checkHasSpace() {
        if (settableSpace() > 0) {
            noSpace = false;
            if (breakingRunnable == null || breakingRunnable.cancelled.get()) startWorking();
        } else {
            if (getMinableBlocks().isEmpty())
                setNoSpace();
        }
    }

    @Override
    void startGetAnimation() {
        List<BlockVec> missing = new ArrayList<>(getMinableBlocks());
        var target = lookAtTarget(missing);
        if (target == null) return;
        new TaskScheduler() {
            double rotation = -90;
            int through;
            int i = 0;

            @Override
            public void run() {
                if (!isRunning) {
                    cancel();
                    return;
                }
                if (rotation >= 0) {
                    rotation = -90;
                    through += 1;
                    if (through == 4) {
                        cancel();
                        var animation = new BlockBreakAnimationPacket(1, target, (byte) 10);
                        instance.getPlayers().forEach(player -> player.sendPacket(animation));
                        var breakPacket = new WorldEventPacket(201, target, minion.representiveBlock().stateId(), false);
                        instance.getPlayers().forEach(player -> player.sendPacket(breakPacket));
                        instance.ensureSetBlockAsync(target, Block.AIR).join();
                        generateLoot();
                        resetPos();
                        return;
                    }
                }
                rotation += 90d / 4d;
                if (rotation >= 0)
                    rotation = 0;
                var armorStandMeta = (ArmorStandMeta) stand.getEntityMeta();
                armorStandMeta.setRightArmRotation(new Vec(rotation, 0, 0));
                var animation = new BlockBreakAnimationPacket(1, target, getBlockBreakStage(i));
                instance.getPlayers().forEach(player -> player.sendPacket(animation));
                i++;
            }
        }.repeatTask(10, 2);
    }

    @Override
    void startGenerateAnimation() {
        List<BlockVec> missing = new ArrayList<>(getPlacebleBlocks());
        var target = lookAtTarget(missing);
        if (target == null) return;
        new TaskScheduler() {
            double rotation = -90;
            int i = 0;

            @Override
            public void run() {
                if (!isRunning) {
                    cancel();
                    return;
                }
                if (rotation >= 0) {
                    cancel();
                    instance.ensureSetBlockAsync(target, minion.representiveBlock());
                    resetPos();
                    return;
                }
                rotation += 90d / 4d;
                var armorStandMeta = (ArmorStandMeta) stand.getEntityMeta();
                armorStandMeta.setRightArmRotation(new Vec(Math.toRadians(rotation), 0, 0));
                i++;
            }
        }.repeatTask(10, 2);

    }

    private byte getBlockBreakStage(int currentTick) {
        double result = ((double) currentTick / (double) 16) * 10;
        return (byte) result;
    }

    @Override
    boolean isMaxGenerated() {
        return getPlacebleBlocks().isEmpty();
    }

    @Override
    int settableSpace() {
        int blocks = 0;
        for (var b : getBlocks()) {
            if (!b.isAir())
                continue;
            blocks++;
        }
        return blocks;
    }

    public Set<Block> getBlocks() {
        Set<Block> blocks = new HashSet<>();
        for (var block : getBlockGrit()) {
                blocks.add(instance.ensureGetBlockAsync(block).join());
        }
        return blocks;
    }

    public Set<BlockVec> getMinableBlocks() {
        Set<BlockVec> blocks = new HashSet<>();
        for (var block : getBlockGrit()) {
            var blockInfo = instance.ensureGetBlockAsync(block).join();
            if (blockInfo.key().equals(((AbstractMiningMinionData) base).representiveBlock().key()))
                blocks.add(block);
        }
        return blocks;
    }

    private Set<BlockVec> getPlacebleBlocks() {
        Set<BlockVec> blocks = new HashSet<>();
        for (var block : getBlockGrit()) {
            var blockInfo = instance.ensureGetBlockAsync(block).join();
            if (blockInfo.isAir())
                blocks.add(block);
        }
        return blocks;
    }
}
