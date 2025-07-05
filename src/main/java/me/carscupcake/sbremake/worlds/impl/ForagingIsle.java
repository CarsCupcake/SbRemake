package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.AbstractNpc;
import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.minestom.server.coordinate.BlockVec;

import java.util.HashMap;
import java.util.List;

public abstract class ForagingIsle extends SkyblockWorld.WorldProvider {
    public final HashMap<BlockVec, Log.LogInfo> brokenLogs = new HashMap<>();
    private final TaskScheduler foragingReset = new TaskScheduler() {
        @Override
        public void run() {
            brokenLogs.forEach((block, log) -> log.regen(container, block));
            brokenLogs.clear();
        }
    };
    public ForagingIsle(List<Launchpad> launchpads, AbstractNpc... npcs) {
        super(launchpads, npcs);
    }

    public ForagingIsle(AbstractNpc... npcs) {
        super(npcs);
    }
    @Override
    protected void register() {
        foragingReset.repeatTask(20 * 30);
    }

    @Override
    protected void unregister() {
        foragingReset.cancel();
    }
}