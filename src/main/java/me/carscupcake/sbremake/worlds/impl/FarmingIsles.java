package me.carscupcake.sbremake.worlds.impl;

import lombok.Getter;
import me.carscupcake.sbremake.blocks.FarmingCrystal;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FarmingIsles extends SkyblockWorld.WorldProvider {
    public static final String FARMING_CRYSTAL_SKIN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI2NWY5NmY1NGI3ODg4NWM0NmU3ZDJmODZiMWMxZGJmZTY0M2M2MDYwZmM3ZmNjOTgzNGMzZTNmZDU5NTEzNSJ9fX0=";
    private List<FarmingCrystal> crystals = new ArrayList<>();
    private final MapList<Integer, BlockVec> sugarCane = new MapList<>();
    private final TaskScheduler sugarCaneRespawn = new TaskScheduler() {
        @Override
        public void run() {
            for (List<BlockVec> blockVecs : sugarCane.values())
                for (BlockVec block : blockVecs)
                    container.setBlock(block, Block.SUGAR_CANE);
            sugarCane.clear();
        }
    };

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.FarmingIsles;
    }

    @Override
    public Pos spawn() {
        return new Pos(113.5, 71, -207.5, -135, 0);
    }

    @Override
    public Region[] regions() {
        return new Region[0];
    }

    @Override
    protected void register() {
        crystals = List.of(new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(125.5, 78, -216.5), container), new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(148.5, 86, -278.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(121.5, 82, -270.5), container), new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(168.5, 87, -264.5), container),
                new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(174.5, 90, -244.5), container), new FarmingCrystal(FARMING_CRYSTAL_SKIN, new Pos(172.5, 85, -218.5), container));
        sugarCaneRespawn.repeatTask(200);
    }

    @Override
    protected void unregister() {
        crystals.forEach(farmingCrystal -> farmingCrystal.task().cancel());
        sugarCaneRespawn.cancel();
    }

    @Override
    public boolean useCustomMining() {
        return false;
    }
}
