package me.carscupcake.sbremake.util.lootTable;

import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.block.FakeBlock;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.*;

public class BossLootTable extends LootTable<SbItemStack> {
    private static final List<Vec> locs = new ArrayList<>(List.of(new Vec(4, 0, 0), new Vec(-4, 0, 0), new Vec(0, 0, 4), new Vec(0, 0, -4)));
    static {
        locs.addAll(getInner());
        locs.remove(Vec.ZERO);
    }

    private static List<Vec> getInner() {
        List<Vec> circleBlocks = new ArrayList<>();
        int bx = Vec.ZERO.blockX();
        int by = Vec.ZERO.blockY();
        int bz = Vec.ZERO.blockZ();
        for (int x = bx - 3; x <= bx + 3; x++) {
            for (int y = by - 3; y <= by + 3; y++) {
                for (int z = bz - 3; z <= bz + 3; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < 3 * 3) {
                        Vec l = new Vec(x, Vec.ZERO.y(), z);
                        if (!circleBlocks.contains(l)) circleBlocks.add(l);
                    }
                }
            }
        }

        return circleBlocks;
    }

    public void spawnLootTable(Instance instance, Pos middle, Collection<SkyblockPlayer> players) {
        var fakes = setBlocks(middle, instance);
        new TaskScheduler() {
            @Override
            public void run() {
                fakes.forEach(FakeBlock::release);
            }
        }.delayTask(20*30);
    }

    public Set<FakeBlock> setBlocks(Pos middle, Instance instance) {
        var blocks = new HashSet<FakeBlock>();
        for (var inner : locs) {
            var pos = middle.add(inner);
            var block = instance.getBlock(pos);
            var fake = FakeBlock.of(instance, new BlockVec(pos), block, Block.IRON_BLOCK);
            fake.setActive(true);
            blocks.add(fake);
        }
        var middleBlock = FakeBlock.of(instance, new BlockVec(middle), instance.getBlock(middle), Block.BEACON);
        middleBlock.setActive(true);
        blocks.add(middleBlock);
        return blocks;
    }
}
